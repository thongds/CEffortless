package com.sip.ceffortless.sound_controller;

/**
 * Created by ssd on 8/17/16.
 */
public class CheapWAV extends  CheapSound {
    public static Factory getFactory() {
        return new Factory() {
            @Override
            public CheapSound create() {
                return  new CheapWAV();
            }

            @Override
            public String[] getSupportExtension() {
                return new String[]{"wav"};
            }
        };
    }
    @Override
    public int getNumFrames() {
        return 0;
    }

    @Override
    public int getSamplesPerFrame() {
        return 0;
    }

    @Override
    public int[] getFrameGains() {
        return new int[0];
    }

    @Override
    public int getFileSizeBytes() {
        return 0;
    }

    @Override
    public int getAvgBitrateKbps() {
        return 0;
    }

    @Override
    public int getSampleRate() {
        return 0;
    }

    @Override
    public int getChannels() {
        return 0;
    }

    @Override
    public String getFiletype() {
        return null;
    }
}
