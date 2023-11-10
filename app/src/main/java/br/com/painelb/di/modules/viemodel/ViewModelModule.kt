package br.com.painelb.di.modules.viemodel

import androidx.lifecycle.ViewModelProvider
import br.com.painelb.di.modules.viemodel.AppViewModelFactory
import dagger.Binds
import dagger.Module

@Suppress("unused")
@Module
abstract class ViewModelModuleFactory {
    @Binds
    abstract fun bindViewModelFactory(factory: AppViewModelFactory): ViewModelProvider.Factory
}
