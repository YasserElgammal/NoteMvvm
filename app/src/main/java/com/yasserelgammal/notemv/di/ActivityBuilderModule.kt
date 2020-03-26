package com.yasserelgammal.notemv.di

import com.yasserelgammal.notemv.ui.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilderModule {

    @ContributesAndroidInjector(modules = [ViewModelModule::class,FragmentBuilderModule::class])
    abstract fun contributeMainActivity(): MainActivity

}