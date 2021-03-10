package com.liweiyap.narradir.utils;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

public class FullScreenPortraitActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        makeFullScreen();
    }

    protected void makeFullScreen()
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
