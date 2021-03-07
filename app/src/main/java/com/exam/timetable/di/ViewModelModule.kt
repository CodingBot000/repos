package com.exam.timetable.di


import com.exam.timetable.viewmodel.*
import org.koin.dsl.module
import org.koin.android.viewmodel.dsl.viewModel

val viewModelModule = module {
    viewModel { MainViewModel(get(), get(), get(), get(), get()) }
    viewModel { DetailViewModel(get(), get(), get(), get()) }
    viewModel { MemoViewModel(get(), get(), get()) }
    viewModel { SearchViewModel(get()) }

}

