package com.eakurnikov.trustore.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.eakurnikov.trustore.ui.TrustoreViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @Binds
    fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(TrustoreViewModel::class)
    fun bindMainViewModel(viewModel: TrustoreViewModel): ViewModel
}
