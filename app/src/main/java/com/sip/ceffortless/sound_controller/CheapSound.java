package com.sip.ceffortless.sound_controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by ssd on 8/16/16.
 */
public abstract class CheapSound {
    protected File mFileInput;
    interface  Factory {
        public CheapSound create();
        public String[] getSupportExtension();
    }
    public static Factory[] supportClass = new Factory[]{
        CheapMp3.getFactory(),
        CheapWAV.getFactory()
    };
    public static HashMap<String,Factory> cheapSoundFactory = new HashMap<>();
    static {
        for(Factory f: supportClass){
            for (String extension : f.getSupportExtension()){
                cheapSoundFactory.put(extension,f);
            }
        }
    }
    public  static CheapSound create(File file) throws FileNotFoundException,IOException{

        String pathFile = file.getAbsolutePath();
        String[] fileComponent = pathFile.split("\\.");
        if(fileComponent.length<2){
            return null;
        }
        Factory factory =  cheapSoundFactory.get(fileComponent[fileComponent.length-1]);
        CheapSound cheapSound = factory.create();
        cheapSound.readFile(file);
        return cheapSound;
    };
    public  void readFile(File f) throws FileNotFoundException,IOException{
        mFileInput = f;
    };
    public abstract int getNumFrames();

    public abstract  int getSamplesPerFrame();

    public abstract  int[] getFrameGains();

    public abstract  int getFileSizeBytes();

    public abstract int getAvgBitrateKbps();

    public abstract int getSampleRate();

    public abstract int getChannels();

    public abstract  String getFiletype();

}
