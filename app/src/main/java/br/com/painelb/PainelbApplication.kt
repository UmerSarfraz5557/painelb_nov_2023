package br.com.painelb

import android.content.Context
import android.content.res.Configuration
import br.com.painelb.base.BaseApplication
import br.com.painelb.di.DaggerAppComponent
import com.zeugmasolutions.localehelper.LocaleHelperApplicationDelegate
import com.zxy.tiny.Tiny
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication

class PainelbApplication : BaseApplication() {

    private val localeAppDelegate = LocaleHelperApplicationDelegate()

    override fun onCreate() {
        context = applicationContext
        super.onCreate()
        Tiny.getInstance().init(this)
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> =
        DaggerAppComponent.factory().create(this)

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(localeAppDelegate.attachBaseContext(base))
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        localeAppDelegate.onConfigurationChanged(this)
    }

    companion object {
        lateinit var context: Context
    }
}