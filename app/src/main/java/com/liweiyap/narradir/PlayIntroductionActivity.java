package com.liweiyap.narradir;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.liweiyap.narradir.utils.FullScreenPortraitActivity;

public class PlayIntroductionActivity extends FullScreenPortraitActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_introduction);

        Button button = findViewById(R.id.backButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
