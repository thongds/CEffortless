package com.sip.ceffortless.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.sip.ceffortless.MainActivity;
import com.sip.ceffortless.R;
import com.sip.ceffortless.adapter.ListLessonAdapter;
import com.sip.ceffortless.model.LessonItem;

import java.util.ArrayList;

/**
 * Created by ssd on 8/14/16.
 */
public class ListLessonFragment extends PFragment implements ListView.OnItemClickListener {
    private MainActivity mMainActivity;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_lesson_fragment,container,false);
        mMainActivity.addTitle("Science");
        ListView listView = (ListView)view.findViewById(R.id.listView);
        ArrayList<LessonItem> lessonItems = new ArrayList<>();
        for (int i = 0 ; i<10; i++){
            LessonItem lessonItem = new LessonItem("title ",i,"12/12/1990");
            lessonItems.add(lessonItem);
        }
        ListLessonAdapter listLessonAdapter = new ListLessonAdapter(getContext(),R.layout.item_layout,lessonItems);
        listView.setOnItemClickListener(this);
        listView.setAdapter(listLessonAdapter);
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        LessonItem itemLesson = (LessonItem)parent.getItemAtPosition(position);
        LessonDetailParent lessonDetailParent = new LessonDetailParent();
        lessonDetailParent.setmItem(itemLesson);
        mMainActivity.replaceForground(lessonDetailParent);
        mMainActivity.changeNavigateMenu(true);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mMainActivity = (MainActivity)activity;
    }

//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        mMainActivity =(MainActivity)getActivity();
//    }
}
