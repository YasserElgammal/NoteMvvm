package com.yasserelgammal.notemv.di

import com.yasserelgammal.notemv.ui.AddFragment
import com.yasserelgammal.notemv.ui.ArchiveFragment
import com.yasserelgammal.notemv.ui.EditFragment
import com.yasserelgammal.notemv.ui.ListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuilderModule {

    @ContributesAndroidInjector
    abstract fun contributeListFragment(): ListFragment

    @ContributesAndroidInjector
    abstract fun contributeAddFragment(): AddFragment

    @ContributesAndroidInjector
    abstract fun contributeEditFragment(): EditFragment

    @ContributesAndroidInjector
    abstract fun contributeArchiveFragment(): ArchiveFragment
}