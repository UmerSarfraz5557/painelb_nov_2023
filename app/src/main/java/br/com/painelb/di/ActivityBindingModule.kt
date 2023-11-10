package br.com.painelb.di

import br.com.painelb.di.modules.auth.AuthModule
import br.com.painelb.di.modules.launcher.LaunchModule
import br.com.painelb.di.modules.main.MainModule
import br.com.painelb.di.scope.ActivityScoped
import br.com.painelb.ui.authentication.AuthenticationActivity
import br.com.painelb.ui.launcher.LauncherActivity
import br.com.painelb.ui.main.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class ActivityBindingModule {

    @ActivityScoped
    @ContributesAndroidInjector(modules = [LaunchModule::class])
    internal abstract fun launcherActivity(): LauncherActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [AuthModule::class])
    internal abstract fun authenticationActivity(): AuthenticationActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [MainModule::class])
    internal abstract fun pwdActivity(): MainActivity
}
