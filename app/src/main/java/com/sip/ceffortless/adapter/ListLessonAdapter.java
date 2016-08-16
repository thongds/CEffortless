package com.sip.ceffortless.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sip.ceffortless.R;
import com.sip.ceffortless.Utilies.UtiliFunction;
import com.sip.ceffortless.model.LessonItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by ssd on 8/14/16.
 */
public class ListLessonAdapter extends ArrayAdapter<LessonItem> {
    private List<LessonItem> mLessonData;
    private  Context mContext;
    private int mResource;
    public ListLessonAdapter(Context context, int resource, List<LessonItem> objects) {
        super(context, resource, objects);
        mLessonData= objects;
        mContext = context;
        mResource =resource;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(mResource,parent,false);
        TextView textView = (TextView)view.findViewById(R.id.tv_rating_label);
        TextView dateTv = (TextView)view.findViewById(R.id.date_label);
        ImageView imageView = (ImageView)view.findViewById(R.id.new_lesson);
        if(position<2){
            imageView.setVisibility(View.VISIBLE);
        }
        Typeface typeface = Typeface.createFromAsset(mContext.getAssets(),"fonts/brush.ttf");
        LinearLayout linearLayout = (LinearLayout)view.findViewById(R.id.rating_label);
        int ratingNumber = mLessonData.get(position).getRatingValue();
        addRatingView(linearLayout,ratingNumber);
        textView.setTypeface(typeface);
        dateTv.setTypeface(typeface);
        return view;
    }

    @Override
    public int getCount() {
        if(mLessonData!=null)
            return mLessonData.size();
        return 0;
    }
    private void addRatingView  (LinearLayout parent, int ratingNumber){
        ratingNumber = ratingNumber>5?5:ratingNumber;
        int notRating = 5-ratingNumber;
        if(ratingNumber>0){
            for (int i = 0;i<ratingNumber;i++){
                parent.addView(generateImgRate(R.drawable.icon_star_yes_2x));
            }
        }
        if(notRating>0){
            for(int i = 0; i<notRating;i++){
                parent.addView(generateImgRate(R.drawable.icon_start_no_2x));
            }
        }
    }
    public ImageView generateImgRate(int resource){
        ImageView imgRatingYes = new ImageView(mContext);
        imgRatingYes.setImageResource(resource);
        imgRatingYes.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        imgRatingYes.setPadding(0,0, UtiliFunction.dpTopxInt(mContext,2),0);
        return imgRatingYes;
    }

}
