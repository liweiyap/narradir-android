package com.liweiyap.narradir.util.audio

sealed class IntroAudioPlayerSegment {
    data class Narration(val mIntroSegmentIndex: Int): IntroAudioPlayerSegment()

    data class Pause(val mRemainingDurationInMilliSecs: Long): IntroAudioPlayerSegment() {
        fun isMinimumDuration(): Boolean {
            return (mRemainingDurationInMilliSecs <= IntroAudioPlayer.sMinPauseDurationInMilliSecs)
        }
    }

    object Error: IntroAudioPlayerSegment()
}