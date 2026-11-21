package com.liweiyap.narradir.util

import android.content.Context
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.google.android.play.agesignals.AgeSignalsAccessRequest
import com.google.android.play.agesignals.AgeSignalsAccessResult
import com.google.android.play.agesignals.AgeSignalsException
import com.google.android.play.agesignals.AgeSignalsManager
import com.google.android.play.agesignals.AgeSignalsManagerFactory
import com.google.android.play.agesignals.AgeSignalsRequest
import com.google.android.play.agesignals.AgeSignalsResult
import com.google.android.play.agesignals.model.AgeSignalsErrorCode
import com.google.android.play.agesignals.model.AgeSignalsStatus
import com.google.android.play.agesignals.testing.FakeAgeSignalsManager
import com.liweiyap.narradir.R
import com.liweiyap.narradir.ui.age.UnderageDialogFragment
import com.liweiyap.narradir.ui.age.VerificationReqDialogFragment

class AgeSignalsManagerWrapper(applicationContext: Context, activity: FragmentActivity) {
    private var mAgeSignalsManager: AgeSignalsManager? = null
    private var mAccessRequest: AgeSignalsAccessRequest? = null
    private var mRequest: AgeSignalsRequest? = null
    private val mUseCase: UseCase = UseCase.DEBUG_VERIFICATION_REQUIRED
    private var mIsResolved: Boolean = false

    private var mContext: Context?
    private var mFragmentManager: FragmentManager? = null

    private enum class UseCase {
        RELEASE,
        DEBUG_SHARED,
        DEBUG_SHARED_UNDERAGE,
        DEBUG_NOT_SHARED,
        DEBUG_VERIFICATION_REQUIRED,
    }

    init {
        mContext = applicationContext
        mFragmentManager = activity.supportFragmentManager

        initAgeSignalsManager()
        mAccessRequest = AgeSignalsAccessRequest.builder()
            .setActivity(activity)
            .build()
        mRequest = AgeSignalsRequest.builder().build()
    }

    fun destroy() {
        mContext = null
        mFragmentManager = null
        mAgeSignalsManager = null
        mAccessRequest = null
        mRequest = null
    }

    private fun initAgeSignalsManager() {
        if (mUseCase == UseCase.RELEASE) {
            mAgeSignalsManager = AgeSignalsManagerFactory.create(mContext!!)
        }
        else {
            val fake = FakeAgeSignalsManager()

            val status: Int = when (mUseCase) {
                UseCase.DEBUG_SHARED, UseCase.DEBUG_SHARED_UNDERAGE -> AgeSignalsStatus.SHARED
                UseCase.DEBUG_NOT_SHARED -> AgeSignalsStatus.NOT_SHARED
                UseCase.DEBUG_VERIFICATION_REQUIRED -> AgeSignalsStatus.VERIFICATION_REQUIRED
            }
            val result = AgeSignalsAccessResult.builder()
                .setAgeSignalsStatus(status)
                .build()
            val user = AgeSignalsResult.builder()
                .setAgeLower(if (mUseCase == UseCase.DEBUG_SHARED_UNDERAGE) 12 else 13)
                .build()

            fake.setNextAgeSignalsAccessResult(result)
            fake.setNextAgeSignalsResult(user)
            mAgeSignalsManager = fake
        }
    }

    fun requestAgeSignals() {
        if (mAccessRequest == null || mIsResolved) {
            return
        }

        mAgeSignalsManager!!.requestAgeSignalsAccess(mAccessRequest!!)
            .addOnSuccessListener { result ->
                // For eligible users in US states with laws requiring app stores to provide verified
                // age information to developers, the in-app prompt is not triggered.
                // Instead, users will be asked to verify or set up supervision when they visit the Play Store app.
                // Use the value of ageSignalsStatus to determine the verification status:
                //  - If the user has completed age verification or parental supervision is active,
                //    the value of ageSignalsStatus is SHARED.
                //  - If the user has not yet verified their age or set up supervision, the value of
                //    ageSignalsStatus is VERIFICATION_REQUIRED.
                //
                // For users in other regions where age sharing is based on the user's or parent's choice:
                //  - If the user's setting is Ask before sharing, the in-app prompt is shown.
                //    If the user agrees to share their age, the value of ageSignalsStatus is SHARED;
                //    otherwise, NOT_SHARED.
                //  - If the user's setting is Always Share, the in-app prompt is not shown,
                //    and the value of ageSignalsStatus is SHARED.
                //  - If the user's setting is Never Share, the in-app prompt is not shown,
                //    and the value of ageSignalsStatus is NOT_SHARED.
                //  - For supervised users, parents can choose to share the age for their child in Family Link app settings.
                //    If the parents choose to share the age the value of ageSignalsStatus will be SHARED
                //    otherwise it will be NOT_SHARED.
                val status: Int? = result.ageSignalsStatus()
                when (status) {
                    AgeSignalsStatus.SHARED -> {
                        // Call checkAgeSignals()
                        // If the user (or parent) has decided to share the age signals, or is in an eligible auto-share region,
                        // then you receive age signals as part of the API response, and you can decide how to handle the response
                        checkSharedAgeSignals()
                    }
                    AgeSignalsStatus.NOT_SHARED -> {
                        // Your app won't get age signals in the API response.
                        // (user didn't share age range, parent rejected the request, or not eligible).
                        // Fallback to default behaviour.
                        mIsResolved = true
                    }
                    AgeSignalsStatus.VERIFICATION_REQUIRED -> {
                        // The user's age is unknown and the user is in an applicable jurisdiction or region
                        // where age verification and age signals sharing is mandatory.
                        // To obtain an age signal from Google Play in these regions, ask the user to
                        // visit the Play Store to resolve their status.
                        val dialog = VerificationReqDialogFragment()
                        dialog.isCancelable = false
                        dialog.show(mFragmentManager!!, mContext!!.getString(R.string.verify_req_dialog_tag))
                    }
                }
            }
            .addOnFailureListener { e ->
                // Handle API/Play Store connection and system errors
                handleFailure(e)
            }
    }

    private fun checkSharedAgeSignals() {
        mAgeSignalsManager!!
            .checkAgeSignals(mRequest!!)
            .addOnSuccessListener { result ->
                mIsResolved = true

                val lower: Int? = result.ageLower()
                // null if ageSignalsStatus is NOT_SHARED or VERIFICATION_REQUIRED,
                // but in the first place, we checkAgeSignals() only if ageSignalsStatus is SHARED.
                if (lower != null && lower < 13) {
                    val dialog = UnderageDialogFragment()
                    dialog.isCancelable = false
                    dialog.show(mFragmentManager!!, mContext!!.getString(R.string.underage_dialog_tag))
                }
            }
            .addOnFailureListener { e ->
                // Handle API/Play Store connection and system errors
                handleFailure(e)
            }
    }

    private fun handleFailure(e: Exception) {
        if (e !is AgeSignalsException) {
            return
        }

        when (e.errorCode) {
            AgeSignalsErrorCode.API_NOT_AVAILABLE -> {
            }
            else -> {
            }
        }
    }
}