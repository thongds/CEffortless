package com.sip.ceffortless.sound_controller;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by ssd on 8/17/16.
 */
public class CheapMp3 extends CheapSound {

    public static  Factory getFactory(){
        return  new Factory() {
            @Override
            public CheapSound create() {
                return  new CheapMp3();
            }

            @Override
            public String[] getSupportExtension() {
                return new String[]{"mp3"};
            }
        };
    }
    // Member variables representing frame data
    private int mNumFrames;
    private int[] mFrameGains;
    private int mFileSize;
    private int mAvgBitRate;
    private int mGlobalSampleRate;
    private int mGlobalChannels;

    // Member variables used during initialization
    private int mMaxFrames;
    private int mBitrateSum;
    private int mMinGain;
    private int mMaxGain;


    public int getNumFrames() {
        return mNumFrames;
    }

    public int getSamplesPerFrame() {
        return 1152;
    }

    public int[] getFrameGains() {
        return mFrameGains;
    }

    public int getFileSizeBytes() {
        return mFileSize;
    }

    public int getAvgBitrateKbps() {
        return mAvgBitRate;
    }

    public int getSampleRate() {
        return mGlobalSampleRate;
    }

    public int getChannels() {
        return mGlobalChannels;
    }

    public String getFiletype() {
        return "MP3";
    }
    public void readFile(File inputFile)
            throws java.io.FileNotFoundException,
            IOException {
        super.readFile(inputFile);
        mNumFrames = 0;
        mMaxFrames = 64;  // This will grow as needed
        mFrameGains = new int[mMaxFrames];
        mBitrateSum = 0;
        mMinGain = 255;
        mMaxGain = 0;

        // No need to handle filesizes larger than can fit in a 32-bit int
        mFileSize = (int)mFileInput.length();

        FileInputStream stream = new FileInputStream(mFileInput);

        int pos = 0;
        int offset = 0;
        int y = 0;
        byte[] buffer = new byte[12];

        while (pos < mFileSize - 12) {
            // Read 12 bytes at a time and look for a sync code (0xFF)
            while (offset < 12) {
                offset += stream.read(buffer, offset, 12 - offset);
            }

            int bufferOffset = 0;
            while (bufferOffset < 12 &&buffer[bufferOffset] != -1)
                bufferOffset++;
            //// TODO: 8/18/16 show progress here
//            if (mProgressListener != null) {
//                boolean keepGoing = mProgressListener.reportProgress(
//                        pos * 1.0 / mFileSize);
//                if (!keepGoing) {
//                    break;
//                }
//            }

            if (bufferOffset > 0) {
                // We didn't find a sync code (0xFF) at position 0;
                // shift the buffer over and try again
                for (int i = 0; i < 12 - bufferOffset; i++) {
                    buffer[i] = buffer[bufferOffset + i];
                }
                pos += bufferOffset;
                offset = 12 - bufferOffset;
                continue;
            }

            // Check for MPEG 1 Layer III or MPEG 2 Layer III codes
            int mpgVersion = 0;
            if (buffer[1] == -6 || buffer[1] == -5) {
                mpgVersion = 1;
            } else if (buffer[1] == -14 || buffer[1] == -13) {
                mpgVersion = 2;
            } else {
                bufferOffset = 1;
                for (int i = 0; i < 12 - bufferOffset; i++)
                    buffer[i] = buffer[bufferOffset + i];
                pos += bufferOffset;
                offset = 12 - bufferOffset;
                continue;
            }

            // The third byte has the bitrate and samplerate
            int bitRate;
            int sampleRate;
            if (mpgVersion == 1) {
                // MPEG 1 Layer III
                bitRate = BITRATES_MPEG1_L3[(buffer[2] & 0xF0) >> 4];
                sampleRate = SAMPLERATES_MPEG1_L3[(buffer[2] & 0x0C) >> 2];
            } else {
                // MPEG 2 Layer III
                bitRate = BITRATES_MPEG2_L3[(buffer[2] & 0xF0) >> 4];
                sampleRate = SAMPLERATES_MPEG2_L3[(buffer[2] & 0x0C) >> 2];
            }

            if (bitRate == 0 || sampleRate == 0) {
                bufferOffset = 2;
                for (int i = 0; i < 12 - bufferOffset; i++)
                    buffer[i] = buffer[bufferOffset + i];
                pos += bufferOffset;
                offset = 12 - bufferOffset;
                continue;
            }

            // From here on we assume the frame is good
            mGlobalSampleRate = sampleRate;
            int padding = (buffer[2] & 2) >> 1;
            int frameLen = 144 * bitRate * 1000 / sampleRate + padding;

            int gain;
            if ((buffer[3] & 0xC0) == 0xC0) {
                // 1 channel
                mGlobalChannels = 1;
                if (mpgVersion == 1) {
                    gain = ((buffer[10] & 0x01) << 7) +
                            ((buffer[11] & 0xFE) >> 1);
                } else {
                    gain = ((buffer[9] & 0x03) << 6) +
                            ((buffer[10] & 0xFC) >> 2);
                }
            } else {
                // 2 channels
                mGlobalChannels = 2;
                if (mpgVersion == 1) {
                    gain = ((buffer[9]  & 0x7F) << 1) +
                            ((buffer[10] & 0x80) >> 7);
                } else {
                    gain = 0;  // ???
                }
            }

            mBitrateSum += bitRate;

            mFrameGains[mNumFrames] = gain;
            if (gain < mMinGain)
                mMinGain = gain;
            if (gain > mMaxGain)
                mMaxGain = gain;

            mNumFrames++;
            if (mNumFrames == mMaxFrames) {
                // We need to grow our arrays.  Rather than naively
                // doubling the array each time, we estimate the exact
                // number of frames we need and add 10% padding.  In
                // practice this seems to work quite well, only one
                // resize is ever needed, however to avoid pathological
                // cases we make sure to always double the size at a minimum.

                mAvgBitRate = mBitrateSum / mNumFrames;
                int totalFramesGuess =
                        ((mFileSize / mAvgBitRate) * sampleRate) / 144000;
                int newMaxFrames = totalFramesGuess * 11 / 10;
                if (newMaxFrames < mMaxFrames * 2)
                    newMaxFrames = mMaxFrames * 2;

                int[] newOffsets = new int[newMaxFrames];
                int[] newLens = new int[newMaxFrames];
                int[] newGains = new int[newMaxFrames];
                for (int i = 0; i < mNumFrames; i++) {
                    newGains[i] = mFrameGains[i];
                }
                mFrameGains = newGains;
                mMaxFrames = newMaxFrames;
            }

            stream.skip(frameLen - 12);
            pos += frameLen;
            offset = 0;
        }
        Log.e("--test--"," y value "+y);
        // We're done reading the file, do some postprocessing
        if (mNumFrames > 0)
            mAvgBitRate = mBitrateSum / mNumFrames;
        else
            mAvgBitRate = 0;
    }

    static private int BITRATES_MPEG1_L3[] = {
            0,  32,  40,  48,  56,  64,  80,  96,
            112, 128, 160, 192, 224, 256, 320,  0 };
    static private int BITRATES_MPEG2_L3[] = {
            0,   8,  16,  24,  32,  40,  48,  56,
            64,  80,  96, 112, 128, 144, 160, 0 };
    static private int SAMPLERATES_MPEG1_L3[] = {
            44100, 48000, 32000, 0 };
    static private int SAMPLERATES_MPEG2_L3[] = {
            22050, 24000, 16000, 0 };
}
