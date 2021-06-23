package com.liweiyap.narradir.ui;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;

/**
 * https://stackoverflow.com/questions/31198923/preventing-android-navigation-and-action-bars-from-appearing-when-switching-acti
 * https://stackoverflow.com/questions/34394916/why-navigation-bar-is-appearing-in-fullscreen-apps-when-clicked-on-popup-menu
 */
public class ActiveFullScreenPortraitActivity extends AppCompatActivity
{
    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        makeFullScreen();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        makeFullScreen();
        return super.onTouchEvent(event);
    }

    @Override
    public void onConfigurationChanged(@NotNull Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        makeFullScreen();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev)
    {
        makeFullScreen();
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus)
        {
            makeFullScreen();
        }
    }

    protected void makeFullScreen()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
        {
            // https://stackoverflow.com/questions/62643517/immersive-fullscreen-on-android-11
            getWindow().setDecorFitsSystemWindows(false);
            WindowInsetsController controller = getWindow().getInsetsController();
            if (controller != null)
            {
                controller.hide(WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars());
                controller.setSystemBarsBehavior(WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
            }
        }
        else
        {
            // make the below show-/hide-changes temporary
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                    // set the content to appear under the system bars
                    // so that the content does not resize when the system bars hide and show
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                    // hide navigation bar
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                    // hide status bar
                    View.SYSTEM_UI_FLAG_FULLSCREEN);
        }
    }
}