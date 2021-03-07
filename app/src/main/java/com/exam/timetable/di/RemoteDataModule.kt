package com.exam.timetable.di

import com.exam.sample.model.datasource.local.favorite.MemoTableDBInfoLocalDataSource
import com.exam.sample.model.datasource.local.favorite.MemoTableDBInfoLocalDataSourceImpl
import com.exam.sample.model.datasource.local.favorite.TimeTableDBInfoLocalDataSourceImpl
import com.exam.timetable.model.datasource.local.timetable.TimeTableDBInfoLocalDataSource
import com.exam.timetable.model.datasource.remote.detail.DetailDataRemoteDataSource
import com.exam.timetable.model.datasource.remote.detail.DetailDataRemoteDataSourceImpl
import com.exam.timetable.model.datasource.remote.memo.MemoDataRemoteDataSource
import com.exam.timetable.model.datasource.remote.memo.MemoDataRemoteDataSourceImpl

import com.exam.timetable.model.datasource.remote.search.SearchRemoteDataSource
import com.exam.timetable.model.datasource.remote.search.SearchRemoteDataSourceImpl
import com.exam.timetable.model.repository.detail.DetailDataRepositoryImpl

import org.koin.core.module.Module
import org.koin.dsl.module


val remoteDataModule: Module = module {

    single<DetailDataRemoteDataSource> { DetailDataRemoteDataSourceImpl(get()) }
    single<MemoDataRemoteDataSource> { MemoDataRemoteDataSourceImpl(get()) }
    single<SearchRemoteDataSource> { SearchRemoteDataSourceImpl(get()) }
}
