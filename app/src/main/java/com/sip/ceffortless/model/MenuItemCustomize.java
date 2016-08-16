package com.sip.ceffortless.model;

import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by ssd on 8/15/16.
 */
public class MenuItemCustomize {
    private ImageView mMenuIcon;
    private  String mMenuTitle ="Social/crime/government";
    private boolean isHasNewLesson;

    public int getmIconResource() {
        return mIconResource;
    }

    public void setmIconResource(int mIconResource) {
        this.mIconResource = mIconResource;
    }

    private int mIconResource;
    public ImageView getmMenuIcon() {
        return mMenuIcon;
    }

    public void setmMenuIcon(ImageView mMenuIcon) {
        this.mMenuIcon = mMenuIcon;
    }

    public String getmMenuTitle() {
        return mMenuTitle;
    }

    public void setmMenuTitle(String mMenuTitle) {
        this.mMenuTitle = mMenuTitle;
    }

    public boolean isHasNewLesson() {
        return isHasNewLesson;
    }

    public void setHasNewLesson(boolean hasNewLesson) {
        isHasNewLesson = hasNewLesson;
    }


}
