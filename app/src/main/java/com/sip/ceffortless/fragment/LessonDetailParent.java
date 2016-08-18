package com.sip.ceffortless.fragment;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.sip.ceffortless.MainActivity;
import com.sip.ceffortless.R;
import com.sip.ceffortless.model.LessonItem;
import com.sip.ceffortless.sound_controller.CheapSound;
import com.sip.ceffortless.view_customize.WaveFormView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by ssd on 8/14/16.
 */
public class LessonDetailParent extends PFragment {
    private MainActivity mMainActivity;
    private LessonItem mItem;
    public LessonItem getmItem() {
        return mItem;
    }

    public void setmItem(LessonItem mItem) {
        this.mItem = mItem;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.lesson_detail_parent,container,false);
        mMainActivity.addTitle("Lesson detail");
        WaveFormView waveFormView = (WaveFormView)view.findViewById(R.id.waveform);
        try {
            String path2 = "/storage/emulated/0/Android/data/com.dropbox.android/files/u197743187/scratch/crazy english origin/tap 3-mp3/Lesson 06.mp3";
            File file = new File(path2);
            CheapSound cheapSound = CheapSound.create(file);
            waveFormView.setSound(cheapSound);
            waveFormView.invalidate();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(mMainActivity,"cant not load file",Toast.LENGTH_LONG).show();
        }

        return  view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mMainActivity = (MainActivity) getActivity();
    }
}
