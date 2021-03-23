package com.liweiyap.narradir;

import android.os.Bundle;
import android.util.Log;

import com.liweiyap.narradir.utils.FullScreenPortraitActivity;
import com.liweiyap.narradir.utils.SettingsLayout;

public class SettingsHomeActivity extends FullScreenPortraitActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_home);

        SettingsLayout narrationSettingsLayout = findViewById(R.id.narrationSettingsLayout);
        narrationSettingsLayout.setKey("NARRATION");

        SettingsLayout backgroundSettingsLayout = findViewById(R.id.backgroundSettingsLayout);
        backgroundSettingsLayout.setKey("BACKGROUND");

        SettingsLayout roleTimerSettingsLayout = findViewById(R.id.roleTimerSettingsLayout);
        roleTimerSettingsLayout.setKey("ROLE TIMER");
    }
}
