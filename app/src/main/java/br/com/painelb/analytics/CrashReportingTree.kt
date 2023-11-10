package br.com.painelb.analytics

import android.util.Log
import com.crashlytics.android.Crashlytics
import timber.log.Timber

class CrashReportingTree : Timber.Tree() {
    override fun log(mPriority: Int, mTag: String?, mMessage: String, mThrowable: Throwable?) {
        if (mPriority == Log.VERBOSE || mPriority == Log.DEBUG) {
            return
        }
        Crashlytics.log(mPriority, mTag, mMessage)
        if (mThrowable != null) {
            Crashlytics.logException(mThrowable)
        }
    }
}
