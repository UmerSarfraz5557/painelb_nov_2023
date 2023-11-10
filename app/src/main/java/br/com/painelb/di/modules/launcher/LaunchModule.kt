package br.com.painelb.di.modules.launcher

import androidx.lifecycle.ViewModel
import br.com.painelb.di.modules.viemodel.ViewModelKey
import br.com.painelb.ui.launcher.LaunchViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Suppress("unused")
@Module
internal abstract class LaunchModule {

    @Binds
    @IntoMap
    @ViewModelKey(LaunchViewModel::class)
    abstract fun bindLaunchViewModel(viewModel: LaunchViewModel): ViewModel
}

