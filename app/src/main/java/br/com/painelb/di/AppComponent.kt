package br.com.painelb.di

import br.com.painelb.PainelbApplication
import br.com.painelb.di.modules.api.ApiModule
import br.com.painelb.di.modules.app.AppModule
import br.com.painelb.di.modules.viemodel.ViewModelModuleFactory
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        AppModule::class,
        ApiModule::class,
        ViewModelModuleFactory::class,
        ActivityBindingModule::class]
)
interface AppComponent : AndroidInjector<PainelbApplication> {

    @Component.Factory
    interface Factory : AndroidInjector.Factory<PainelbApplication>
}
