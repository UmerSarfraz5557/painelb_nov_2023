package br.com.painelb.di.modules.auth

import androidx.lifecycle.ViewModel
import br.com.painelb.di.modules.viemodel.ViewModelKey
import br.com.painelb.di.scope.FragmentScoped
import br.com.painelb.ui.authentication.fragments.forgetpassword.ForgetPasswordFragment
import br.com.painelb.ui.authentication.fragments.login.LoginFragment
import br.com.painelb.ui.authentication.fragments.login.LoginViewModel
import br.com.painelb.ui.authentication.fragments.registration.RegistrationFragment
import br.com.painelb.ui.authentication.fragments.registration.RegistrationViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Suppress("unused")
@Module
internal abstract class AuthModule {

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    internal abstract fun bindLoginViewModel(viewModel: LoginViewModel): ViewModel

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeLoginFragment(): LoginFragment

    @Binds
    @IntoMap
    @ViewModelKey(RegistrationViewModel::class)
    internal abstract fun bindRegistrationViewModel(viewModel: RegistrationViewModel): ViewModel

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeRegistrationFragment(): RegistrationFragment

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeForgetPasswordFragment(): ForgetPasswordFragment
}
