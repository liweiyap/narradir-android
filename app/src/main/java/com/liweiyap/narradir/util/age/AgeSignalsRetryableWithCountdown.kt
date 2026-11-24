package com.liweiyap.narradir.util.age

enum class AgeSignalsRetryableWithCountdown(
    private val waitDurationMillis: LongArray,
    val waitDurationStepMillis: Long,
    val retryMax: Int,
) {
    CANNOT_BIND_TO_SERVICE(waitDurationMillis = longArrayOf(15000L, 30000L, 60000L), waitDurationStepMillis = 1000L, retryMax = 3),
    CLIENT_TRANSIENT_ERROR(waitDurationMillis = longArrayOf(5000L), waitDurationStepMillis = 1000L, retryMax = 3),
    INTERNAL_ERROR(waitDurationMillis = longArrayOf(5000L, 15000L, 60000L), waitDurationStepMillis = 1000L, retryMax = 3);

    fun getCurrentWaitDuration(retryCount: Int?): Long {
        if (waitDurationMillis.isEmpty()) {
            throw RuntimeException(
                "AgeSignalsRetryableWithCountdown::getCurrentWaitDuration(): " +
                    "no wait durations declared")
        }

        if (retryCount == null) {
            return waitDurationMillis.first()
        }

        if (retryCount < 0) {
            throw RuntimeException(
                "AgeSignalsRetryableWithCountdown::getCurrentWaitDuration(): " +
                    "retry count must be >= 0")
        }

        if (retryCount >= waitDurationMillis.size) {
            return waitDurationMillis.last()
        }

        return waitDurationMillis[retryCount]
    }
}