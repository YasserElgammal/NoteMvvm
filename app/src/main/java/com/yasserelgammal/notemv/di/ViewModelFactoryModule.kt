package com.yasserelgammal.notemv.di

import androidx.lifecycle.ViewModelProvider
import com.yasserelgammal.notemv.util.ViewModelProviderFactory
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelFactoryModule {
    @Binds
    abstract fun bindViewModelFactory(viewModelProvideFactory: ViewModelProviderFactory): ViewModelProvider.Factory
}