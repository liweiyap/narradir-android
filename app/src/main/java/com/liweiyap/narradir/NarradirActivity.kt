package com.liweiyap.narradir

import android.content.Context
import android.os.Build
import android.os.Bundle

import com.liweiyap.narradir.ui.FullScreenActivity
import com.liweiyap.narradir.util.INarradirControl
import com.liweiyap.narradir.util.NarradirViewModel
import com.liweiyap.narradir.util.audio.ClickSoundGenerator

class NarradirActivity: FullScreenActivity(), INarradirControl {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_narradir)

        viewModel = NarradirViewModel(this, getSharedPreferences(getString(R.string.shared_preferences_key), Context.MODE_PRIVATE))
        mClickSoundGenerator = ClickSoundGenerator(this)
    }

    override fun onDestroy() {
        super.onDestroy()

        mClickSoundGenerator?.freeResources()
        mClickSoundGenerator = null

        viewModel?.destroy()
        viewModel = null
    }

    override fun playClickSound() {
        mClickSoundGenerator?.playClickSound()
    }

    override fun navigateAwayFromApp() {
        // https://developer.android.com/guide/components/activities/tasks-and-back-stack#back-press-behavior
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            moveTaskToBack(true)
        }
        else {
            finish()
        }
    }

    private var mClickSoundGenerator: ClickSoundGenerator? = null
    override var viewModel: NarradirViewModel? = null
        private set
}