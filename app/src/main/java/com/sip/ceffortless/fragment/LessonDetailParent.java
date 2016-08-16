package com.sip.ceffortless.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sip.ceffortless.MainActivity;
import com.sip.ceffortless.R;
import com.sip.ceffortless.model.LessonItem;

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
        return  view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mMainActivity = (MainActivity) getActivity();
    }
}
