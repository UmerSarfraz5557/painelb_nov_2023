package br.com.painelb.base.ui

import android.content.Context
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import br.com.painelb.base.navigation.ActivityScreenSwitcher
import com.zeugmasolutions.localehelper.LocaleHelperActivityDelegateImpl
import dagger.android.support.DaggerAppCompatActivity
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import javax.inject.Inject

abstract class BaseActivity<T : ViewDataBinding> constructor(@LayoutRes private val mContentLayoutId: Int) :
    DaggerAppCompatActivity(),
    NavigationHost {
    @Inject
    lateinit var mActivityScreenSwitcher: ActivityScreenSwitcher

    protected val mBinding: T by lazy(LazyThreadSafetyMode.NONE) {
        DataBindingUtil.setContentView<T>(this, mContentLayoutId)
    }

    private val localeDelegate = LocaleHelperActivityDelegateImpl()

    override fun attachBaseContext(newBase: Context) {
        val localeDelegate = LocaleHelperActivityDelegateImpl()
        val context = localeDelegate.attachBaseContext(newBase)
        super.attachBaseContext( ViewPumpContextWrapper.wrap(context))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding.lifecycleOwner = this
    }

    override fun onResume() {
        mActivityScreenSwitcher.attach(this)
        super.onResume()
    }

    override fun onPause() {
        mActivityScreenSwitcher.detach()
        super.onPause()
    }

    override fun registerToolbarWithNavigation(toolbar: Toolbar) {
    }

    protected fun onArrowClick() = mActivityScreenSwitcher.goBack()

    override fun activityScreenSwitcher() = mActivityScreenSwitcher
}
