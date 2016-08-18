package com.sip.ceffortless;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sip.ceffortless.adapter.MenuAdapter;
import com.sip.ceffortless.fragment.MainFragment;
import com.sip.ceffortless.fragment.PFragment;
import com.sip.ceffortless.model.LessonItem;
import com.sip.ceffortless.model.MenuItemCustomize;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by ssd on 8/13/16.
 */
public class MainActivity extends FragmentActivity implements View.OnClickListener{
    private FragmentManager mFragmentManager;
    private boolean mIsBackgroundOnTop;
    private ImageView mNavigateMenu;
    private TextView mTitlel;
    private Stack<String> mStackTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStackTitle = new Stack<>();
        setContentView(R.layout.main_activity);
        generateMenu();
        mNavigateMenu = (ImageView)findViewById(R.id.navigate);
        mTitlel = (TextView)findViewById(R.id.header_title);
        mFragmentManager = getSupportFragmentManager();
        ImageView menu = (ImageView)findViewById(R.id.navigate);
        TextView textView = (TextView)findViewById(R.id.day_left);
        Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/brush.ttf");
        textView.setTypeface(typeface);
        menu.setOnClickListener(this);
        MainFragment main = new MainFragment();
        replaceBackground(main);
    }
    public void replaceForground (PFragment fragment){
        if(mIsBackgroundOnTop){
            popBackStack();
        }
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.contain,fragment,fragment.getClass().getSimpleName()).
                    addToBackStack(fragment.getClass().getSimpleName()).commit();
        mIsBackgroundOnTop = false;
    }
    public void replaceBackground (PFragment fragment){
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.background,fragment,fragment.getClass().getSimpleName())
                .addToBackStack(fragment.getClass().getSimpleName()).
                commit();
        mIsBackgroundOnTop = true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        popBackStack();

    }
    public void addTitle(String title){
        mStackTitle.push(title);
        updateTitle();
    }
    public void  updateTitle(){
        if(!mStackTitle.isEmpty()){
             mTitlel.setText(mStackTitle.peek());
        }
    }
    public void changeNavigateMenu(boolean showBackMenu){
        DrawerLayout drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);


        if(showBackMenu){
            mNavigateMenu.setImageResource(R.drawable.back_menu);
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }else{
            mNavigateMenu.setImageResource(R.drawable.menu_ico);
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }
    }

    private void popBackStack(){
        if(!mStackTitle.isEmpty()){
            mStackTitle.pop();
            updateTitle();
        }
        if(mIsBackgroundOnTop || mFragmentManager.getBackStackEntryCount()>1){
            mFragmentManager.popBackStack();
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(mFragmentManager.getBackStackEntryCount()>1){
                    changeNavigateMenu(true);

                }
                else {
                    changeNavigateMenu(false);
                }
            }
        },100);

    }
    @Override
    public void onClick(View v) {
        DrawerLayout drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        switch (v.getId()){
            case R.id.navigate:
                if(mFragmentManager.getBackStackEntryCount()<2)
                    drawerLayout.openDrawer(GravityCompat.START);

                else
                    popBackStack();
                break;

        }
    }
    public void generateMenu(){
        ListView listView = (ListView)findViewById(R.id.list_view_menu);
        ArrayList<MenuItemCustomize> menuItemArrayList = new ArrayList<>();
        int[] resourceIcon = new int[20];
        resourceIcon[0] = R.drawable.icon_airplane;
        resourceIcon[1] = R.drawable.icon_bicycle;
        resourceIcon[2] = R.drawable.icon_directions;
        resourceIcon[3] = R.drawable.icon_eiffel_tower;
        resourceIcon[4] = R.drawable.icon_glasses;
        resourceIcon[5] = R.drawable.icon_helm;
        resourceIcon[6] = R.drawable.icon_palm_tree;
        resourceIcon[7] = R.drawable.icon_home;
        for(int i=0;i<8;i++){
            MenuItemCustomize menuItemCustomize = new MenuItemCustomize();
            if(i==3){
                menuItemCustomize.setHasNewLesson(true);
            }
            if(i == 7){
                menuItemCustomize.setmMenuTitle("Home");
            }
            menuItemCustomize.setmIconResource(resourceIcon[i]);
            menuItemArrayList.add(menuItemCustomize);
        }
        MenuAdapter menuAdapter = new MenuAdapter(this,R.layout.item_menu,menuItemArrayList);
        listView.setAdapter(menuAdapter);
    }
}
