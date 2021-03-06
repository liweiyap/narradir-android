package com.liweiyap.narradir;

import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.TextView;

public class CharacterSelectionActivity extends FullScreenPortraitActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setGameTitleTextViewColours();

        final MediaPlayer mp = MediaPlayer.create(this, R.raw.testwavenet);
        mp.start();
//        mp.release();
    }

    private void setGameTitleTextViewColours()
    {
        TextView gameTitleTextView = findViewById(R.id.game_title);

        int[] colourArray = new int[]{ getColor(R.color.yuma), getColor(R.color.husk) };
        Shader gameTitleTextViewShader = new LinearGradient(0, 0, 0, gameTitleTextView.getTextSize(), colourArray, null, Shader.TileMode.CLAMP);

        gameTitleTextView.getPaint().setShader(gameTitleTextViewShader);
        gameTitleTextView.setTextColor(colourArray[0]);
    }
}