package com.sip.ceffortless.Utilies;

import android.content.Context;

/**
 * Created by ssd on 8/14/16.
 */
public  class UtiliFunction {
    public static  float dpTopixcel(Context context,int dp){
        return dp * context.getResources().getDisplayMetrics().density;
    }
    public static int dpTopxInt(Context context,int dp){
        return (int)( dpTopixcel(context,dp) +0.5f );
    }
}
