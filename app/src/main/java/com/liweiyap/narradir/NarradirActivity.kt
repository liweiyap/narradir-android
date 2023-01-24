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

        mViewModel = NarradirViewModel(this, getSharedPreferences(getString(R.string.shared_preferences_key), Context.MODE_PRIVATE))
        mClickSoundGenerator = ClickSoundGenerator(this)
    }

    override fun onDestroy() {
        super.onDestroy()

        mClickSoundGenerator?.freeResources()
        mClickSoundGenerator = null

        mViewModel?.destroy()
        mViewModel = null
    }

    override fun getViewModel(): NarradirViewModel? {
        return mViewModel
    }

    override fun playClickSound() {
        mClickSoundGenerator?.playClickSound()
    }

    // useful in SecretHitlerCharacterSelectionFragment since popUpToInclusive is TRUE in nav_graph.xml
    // we don't really need it in AvalonCharacterSelectionFragment, but just putting it there too to make sure that app behaviour is consistent throughout
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
    private var mViewModel: NarradirViewModel? = null
}