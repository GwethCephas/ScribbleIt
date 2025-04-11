package com.ceph.scribbleit.di

import androidx.room.Room
import com.ceph.scribbleit.data.local.ScribbleDatabase
import com.ceph.scribbleit.data.repository.ScribbleRepositoryImpl
import com.ceph.scribbleit.domain.repository.ScribbleRepository
import com.ceph.scribbleit.presentation.homeScreen.HomeViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single {
        Room.databaseBuilder(
            androidApplication(),
            ScribbleDatabase::class.java,
            "scribble_db"
        ).build()
    }

    single { get<ScribbleDatabase>().dao }


    single<ScribbleRepository> { ScribbleRepositoryImpl(get()) }

    viewModel { HomeViewModel(get()) }
}