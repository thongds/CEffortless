package com.sip.ceffortless.model;

/**
 * Created by ssd on 8/14/16.
 */
public class LessonItem {
    private String lessonTitle;
    private int ratingValue;
    private String createDate;
    public LessonItem(String lessonTitle,int ratingValue,String createDate){
        setLessonTitle(lessonTitle);
        setRatingValue(ratingValue);
        setCreateDate(createDate);
    }
    public String getLessonTitle() {
        return lessonTitle;
    }

    public void setLessonTitle(String lessonTitle) {
        this.lessonTitle = lessonTitle;
    }

    public int getRatingValue() {
        return ratingValue;
    }

    public void setRatingValue(int ratingValue) {
        this.ratingValue = ratingValue;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }


}
