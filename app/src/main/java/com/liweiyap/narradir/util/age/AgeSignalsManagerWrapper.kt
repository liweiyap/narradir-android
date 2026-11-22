package com.liweiyap.narradir.util.age

import android.content.Context
import android.os.Bundle
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
import com.liweiyap.narradir.ui.DialogHelper
import com.liweiyap.narradir.ui.age.RetryInfiniteDialogFragment
import com.liweiyap.narradir.ui.age.UnderageDialogFragment
import com.liweiyap.narradir.ui.age.VerificationReqDialogFragment

class AgeSignalsManagerWrapper(applicationContext: Context, activity: FragmentActivity) {
    private var mAgeSignalsManager: AgeSignalsManager? = null
    private var mAccessRequest: AgeSignalsAccessRequest? = null
    private var mRequest: AgeSignalsRequest? = null
    private val mUseCase: UseCase = UseCase.DEBUG_APP_NOT_OWNED
    private var mIsResolved: Boolean = false

    private var mContext: Context?
    private var mFragmentManager: FragmentManager? = null

    private enum class UseCase {
        RELEASE,
        DEBUG_SHARED,
        DEBUG_SHARED_UNDERAGE,
        DEBUG_NOT_SHARED,
        DEBUG_VERIFICATION_REQUIRED,
        DEBUG_API_NOT_AVAILABLE,
        DEBUG_PLAY_STORE_NOT_FOUND,
        DEBUG_NETWORK_ERROR,
        DEBUG_PLAY_SERVICES_NOT_FOUND,
        DEBUG_PLAY_STORE_VERSION_OUTDATED,
        DEBUG_PLAY_SERVICES_VERSION_OUTDATED,
        DEBUG_APP_NOT_OWNED,
        DEBUG_DEVICE_NOT_SUPPORTED,
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
                else -> AgeSignalsStatus.SHARED
            }

            val errorCode: Int = when (mUseCase) {
                UseCase.DEBUG_API_NOT_AVAILABLE -> AgeSignalsErrorCode.API_NOT_AVAILABLE
                UseCase.DEBUG_PLAY_STORE_NOT_FOUND -> AgeSignalsErrorCode.PLAY_STORE_NOT_FOUND
                UseCase.DEBUG_NETWORK_ERROR -> AgeSignalsErrorCode.NETWORK_ERROR
                UseCase.DEBUG_PLAY_SERVICES_NOT_FOUND -> AgeSignalsErrorCode.PLAY_SERVICES_NOT_FOUND
                UseCase.DEBUG_PLAY_STORE_VERSION_OUTDATED -> AgeSignalsErrorCode.PLAY_STORE_VERSION_OUTDATED
                UseCase.DEBUG_PLAY_SERVICES_VERSION_OUTDATED -> AgeSignalsErrorCode.PLAY_SERVICES_VERSION_OUTDATED
                UseCase.DEBUG_APP_NOT_OWNED -> AgeSignalsErrorCode.APP_NOT_OWNED
                UseCase.DEBUG_DEVICE_NOT_SUPPORTED -> AgeSignalsErrorCode.DEVICE_NOT_SUPPORTED
                else -> AgeSignalsErrorCode.NO_ERROR
            }

            val result = AgeSignalsAccessResult.builder()
                .setAgeSignalsStatus(status)
                .build()
            val user = AgeSignalsResult.builder()
                .setAgeLower(if (mUseCase == UseCase.DEBUG_SHARED_UNDERAGE) 12 else 13)
                .build()

            fake.setNextAgeSignalsAccessResult(result)
            fake.setNextAgeSignalsResult(user)
            if (errorCode != AgeSignalsErrorCode.NO_ERROR) {
                fake.setNextAgeSignalsException(AgeSignalsException(errorCode))
            }

            mAgeSignalsManager = fake
        }
    }

    fun requestAgeSignals(doIgnoreOldDialog: Boolean) {
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
                        checkSharedAgeSignals(doIgnoreOldDialog)
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
                        DialogHelper.showDialog(
                            VerificationReqDialogFragment(),
                            null,
                            mFragmentManager,
                            mContext!!.getString(R.string.verify_req_dialog_tag),
                            doIgnoreOldDialog)
                    }
                }
            }
            .addOnFailureListener { e ->
                // Handle API/Play Store connection and system errors
                handleFailure(e, AgeSignalsStep.REQUEST_ACCESS, doIgnoreOldDialog)
            }
    }

    fun checkSharedAgeSignals(doIgnoreOldDialog: Boolean) {
        mAgeSignalsManager!!
            .checkAgeSignals(mRequest!!)
            .addOnSuccessListener { result ->
                mIsResolved = true

                val lower: Int? = result.ageLower()
                // null if ageSignalsStatus is NOT_SHARED or VERIFICATION_REQUIRED,
                // but in the first place, we checkAgeSignals() only if ageSignalsStatus is SHARED.
                if (lower != null && lower < 13) {
                    DialogHelper.showDialog(
                        UnderageDialogFragment(),
                        null,
                        mFragmentManager,
                        mContext!!.getString(R.string.underage_dialog_tag),
                        doIgnoreOldDialog)
                }
            }
            .addOnFailureListener { e ->
                // Handle API/Play Store connection and system errors
                handleFailure(e, AgeSignalsStep.CHECK_SHARED, doIgnoreOldDialog)
            }
    }

    private fun handleFailure(e: Exception, step: AgeSignalsStep, doIgnoreOldDialog: Boolean) {
        if (e !is AgeSignalsException) {
            return
        }

        when (e.errorCode) {
            AgeSignalsErrorCode.API_NOT_AVAILABLE,
            AgeSignalsErrorCode.PLAY_STORE_NOT_FOUND,
            AgeSignalsErrorCode.NETWORK_ERROR,
            AgeSignalsErrorCode.PLAY_SERVICES_NOT_FOUND,
            AgeSignalsErrorCode.PLAY_STORE_VERSION_OUTDATED,
            AgeSignalsErrorCode.PLAY_SERVICES_VERSION_OUTDATED -> {
                val args = Bundle()
                args.putString(
                    mContext!!.getString(R.string.retry_infinite_dialog_title_tag),
                    getDialogTitleFromAgeSignalsErrorCode(e.errorCode))
                args.putString(
                    mContext!!.getString(R.string.retry_infinite_dialog_msg_tag),
                    getDialogMessageFromAgeSignalsErrorCode(e.errorCode))
                args.putSerializable(
                    mContext!!.getString(R.string.age_signals_step_enum_tag),
                    step)

                DialogHelper.showDialog(
                    RetryInfiniteDialogFragment(),
                    args,
                    mFragmentManager,
                    getDialogTagFromAgeSignalsErrorCode(e.errorCode),
                    doIgnoreOldDialog)
            }
            AgeSignalsErrorCode.APP_NOT_OWNED,
            AgeSignalsErrorCode.DEVICE_NOT_SUPPORTED -> {
                mIsResolved = true
            }
            else -> {
            }
        }
    }

    private fun getDialogTitleFromAgeSignalsErrorCode(errorCode: Int): String? {
        return when (errorCode) {
            AgeSignalsErrorCode.API_NOT_AVAILABLE -> mContext!!.getString(R.string.api_not_available_dialog_title)
            AgeSignalsErrorCode.PLAY_STORE_NOT_FOUND -> mContext!!.getString(R.string.playstore_not_found_dialog_title)
            AgeSignalsErrorCode.NETWORK_ERROR -> mContext!!.getString(R.string.network_error_dialog_title)
            AgeSignalsErrorCode.PLAY_SERVICES_NOT_FOUND -> mContext!!.getString(R.string.playsvcs_not_found_dialog_title)
            AgeSignalsErrorCode.PLAY_STORE_VERSION_OUTDATED -> mContext!!.getString(R.string.playstore_outdated_dialog_title)
            AgeSignalsErrorCode.PLAY_SERVICES_VERSION_OUTDATED -> mContext!!.getString(R.string.playsvcs_outdated_dialog_title)
            else -> null
        }
    }

    private fun getDialogMessageFromAgeSignalsErrorCode(errorCode: Int): String? {
        return when (errorCode) {
            AgeSignalsErrorCode.API_NOT_AVAILABLE -> mContext!!.getString(R.string.api_not_available_dialog_msg)
            AgeSignalsErrorCode.PLAY_STORE_NOT_FOUND -> mContext!!.getString(R.string.playstore_not_found_dialog_msg)
            AgeSignalsErrorCode.NETWORK_ERROR -> mContext!!.getString(R.string.network_error_dialog_msg)
            AgeSignalsErrorCode.PLAY_SERVICES_NOT_FOUND -> mContext!!.getString(R.string.playsvcs_not_found_dialog_msg)
            AgeSignalsErrorCode.PLAY_STORE_VERSION_OUTDATED -> mContext!!.getString(R.string.playstore_outdated_dialog_msg)
            AgeSignalsErrorCode.PLAY_SERVICES_VERSION_OUTDATED -> mContext!!.getString(R.string.playsvcs_outdated_dialog_msg)
            else -> null
        }
    }

    private fun getDialogTagFromAgeSignalsErrorCode(errorCode: Int): String? {
        return when (errorCode) {
            AgeSignalsErrorCode.API_NOT_AVAILABLE -> mContext!!.getString(R.string.api_not_available_dialog_tag)
            AgeSignalsErrorCode.PLAY_STORE_NOT_FOUND -> mContext!!.getString(R.string.playstore_not_found_dialog_tag)
            AgeSignalsErrorCode.NETWORK_ERROR -> mContext!!.getString(R.string.network_error_dialog_tag)
            AgeSignalsErrorCode.PLAY_SERVICES_NOT_FOUND -> mContext!!.getString(R.string.playsvcs_not_found_dialog_tag)
            AgeSignalsErrorCode.PLAY_STORE_VERSION_OUTDATED -> mContext!!.getString(R.string.playstore_outdated_dialog_tag)
            AgeSignalsErrorCode.PLAY_SERVICES_VERSION_OUTDATED -> mContext!!.getString(R.string.playsvcs_outdated_dialog_tag)
            else -> null
        }
    }
}