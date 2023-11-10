package br.com.painelb.base

import android.content.Context
import android.content.res.Configuration
import br.com.painelb.analytics.CrashReportingTree
import br.com.painelb.BuildConfig
import br.com.painelb.R
import com.crashlytics.android.Crashlytics
import com.facebook.stetho.Stetho
import com.zeugmasolutions.localehelper.LocaleHelperApplicationDelegate
import dagger.android.support.DaggerApplication
import io.fabric.sdk.android.Fabric
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump
import timber.log.Timber

abstract class BaseApplication : DaggerApplication() {
    private val DEFAULT_FONT_PATH = "fonts/Roboto-RobotoRegular.ttf"
    private val localeAppDelegate = LocaleHelperApplicationDelegate()

    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
        initCalligraphy()
        // initCrashReport()
        initTimber()
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(localeAppDelegate.attachBaseContext(base))
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        localeAppDelegate.onConfigurationChanged(this)
    }

    private fun initCalligraphy() {
        val mCalligraphyConfig = CalligraphyConfig.Builder()
            // Todo: 07/25/2019 add default here
            // .setDefaultFontPath(DEFAULT_FONT_PATH)
            .setFontAttrId(R.attr.fontPath)
            .build()
        val mInterceptor = CalligraphyInterceptor(mCalligraphyConfig)
        val mViewPumpBuilder = ViewPump.builder()
            .addInterceptor(mInterceptor)
            .build()
        ViewPump.init(mViewPumpBuilder)
    }

    private fun initCrashReport() {
        Fabric.with(this, Crashlytics())
        Thread.setDefaultUncaughtExceptionHandler { _, ex ->
            Crashlytics.logException(ex)
        }
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            val mdDebugTree = Timber.DebugTree()
            Timber.plant(mdDebugTree)
        } else {
            val mCrashReportingTree = CrashReportingTree()
            Timber.plant(mCrashReportingTree)
        }
    }
}
