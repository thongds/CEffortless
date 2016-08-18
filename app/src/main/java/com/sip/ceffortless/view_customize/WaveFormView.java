package com.sip.ceffortless.view_customize;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.sip.ceffortless.sound_controller.CheapSound;

/**
 * Created by ssd on 8/16/16.
 */
public class WaveFormView extends View {
    private CheapSound mSoundFile;
    private float[] mZoomFactorByZoomLevel;
    private int mZoomLevel;
    protected float range;
    protected float scaleFactor;
    protected float minGain;

    public WaveFormView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getMeasuredWidth();
        int heigh = getMeasuredHeight();
        int ctr = heigh/2;
        int start = 0;
        int i = 0;
        while (i<width){
            int h = (int) (getScaledHeight(mZoomFactorByZoomLevel[mZoomLevel], start + i) *( getMeasuredHeight() / 2));
            drawWaveform(canvas,i,h,ctr,new Paint());
            i++;
        }

    }
    private void drawWaveform(Canvas canvas,int i,int h,int ctr,Paint paint){
        drawWaveformLine(
                canvas, i,
                ctr - h,
                ctr + 1 + h,
                paint);
    }

    private void drawWaveformLine(Canvas canvas,int startX,int startY,int endY,Paint paint){
       canvas.drawLine(startX,startY,startX,endY,paint);
    }
    public void setSound(CheapSound cheapSound){
        mSoundFile =cheapSound;
        computeDoubleForAllLevel();
    }
    public void computeDoubleForAllLevel(){
        mZoomFactorByZoomLevel = new float[4];
        mZoomLevel =2;
        int numFrames = mSoundFile.getNumFrames();

        // Make sure the range is no more than 0 - 255
        float maxGain = 1.0f;
        for (int i = 0; i < numFrames; i++) {
            float gain = getGain(i, numFrames, mSoundFile.getFrameGains());
            if (gain > maxGain) {
                maxGain = gain;
            }
        }
        scaleFactor = 1.0f;
        if (maxGain > 255.0) {
            scaleFactor = 255 / maxGain;
        }

        // Build histogram of 256 bins and figure out the new scaled max
        maxGain = 0;
        int gainHist[] = new int[256];
        for (int i = 0; i < numFrames; i++) {
            int smoothedGain = (int) (getGain(i, numFrames, mSoundFile.getFrameGains()) * scaleFactor);
            if (smoothedGain < 0)
                smoothedGain = 0;
            if (smoothedGain > 255)
                smoothedGain = 255;

            if (smoothedGain > maxGain)
                maxGain = smoothedGain;

            gainHist[smoothedGain]++;
        }

        // Re-calibrate the min to be 5%
        minGain = 0;
        int sum = 0;
        while (minGain < 255 && sum < numFrames / 20) {
            sum += gainHist[(int) minGain];
            minGain++;
        }

        // Re-calibrate the max to be 99%
        sum = 0;
        while (maxGain > 2 && sum < numFrames / 100) {
            sum += gainHist[(int) maxGain];
            maxGain--;
        }
        float ratio = getMeasuredWidth() / (float) numFrames;
        range = maxGain - minGain;
        mZoomFactorByZoomLevel[0] = ratio;
        mZoomFactorByZoomLevel[1] = 1.0f;
        mZoomFactorByZoomLevel[2] = 2.0f;
        mZoomFactorByZoomLevel[3] = 3.0f;
    }
    // block compute high of sample

    protected float getScaledHeight(float zoomLevel, int i) {
        if (zoomLevel == 1.0f) {
            return getNormalHeight(i);
        }else{
            return getZoomedInHeight(zoomLevel,i);
        }
    }
    protected float getNormalHeight(int i) {
        return getHeight(i, mSoundFile.getNumFrames(), mSoundFile.getFrameGains(), scaleFactor, minGain, range);
    }
    protected float getHeight(int i, int numFrames, int[] frameGains, float scaleFactor, float minGain, float range) {
        float value = (getGain(i, numFrames, frameGains) * scaleFactor - minGain) / range;
        if (value < 0.0)
            value = 0.0f;
        if (value > 1.0)
            value = 1.0f;
        return value;
    }
    protected float getGain(int i, int numFrames, int[] frameGains) {
        int x = Math.min(i, numFrames - 1);
        if (numFrames < 2) {
            return frameGains[x];
        } else {
            if (x == 0) {
                return (frameGains[0] / 2.0f) + (frameGains[1] / 2.0f);
            } else if (x == numFrames - 1) {
                return (frameGains[numFrames - 2] / 2.0f) + (frameGains[numFrames - 1] / 2.0f);
            } else {
                return (frameGains[x - 1] / 3.0f) + (frameGains[x] / 3.0f) + (frameGains[x + 1] / 3.0f);
            }
        }
    }
    protected float getZoomedInHeight(float zoomLevel, int i) {
        int f = (int) zoomLevel;
        if (i == 0) {
            return 0.5f * getHeight(0, mSoundFile.getNumFrames(), mSoundFile.getFrameGains(), scaleFactor, minGain, range);
        }
        if (i == 1) {
            return getHeight(0, mSoundFile.getNumFrames(), mSoundFile.getFrameGains(), scaleFactor, minGain, range);
        }
        if (i % f == 0) {
            float x1 = getHeight(i / f - 1, mSoundFile.getNumFrames(), mSoundFile.getFrameGains(), scaleFactor, minGain, range);
            float x2 = getHeight(i / f, mSoundFile.getNumFrames(), mSoundFile.getFrameGains(), scaleFactor, minGain, range);
            return 0.5f * (x1 + x2);
        } else if ((i - 1) % f == 0) {
            return getHeight((i - 1) / f, mSoundFile.getNumFrames(), mSoundFile.getFrameGains(), scaleFactor, minGain, range);
        }
        return 0;
    }
}
