package com.exam.timetable.di

import androidx.room.Room


import com.exam.sample.model.datasource.local.favorite.*

import com.exam.timetable.database.AppDatabase
import com.exam.timetable.database.MemoDao
import com.exam.timetable.database.TimeTableDao
import com.exam.timetable.model.datasource.local.timetable.TimeTableDBInfoLocalDataSource
import com.exam.timetable.utils.Const
import org.koin.core.module.Module
import org.koin.dsl.module


val localDataModule: Module = module {

    single<TimeTableDBInfoLocalDataSource> { TimeTableDBInfoLocalDataSourceImpl(get()) }
    single<TimeTableDao> { get<AppDatabase>().timeTableDao() }
    single<MemoTableDBInfoLocalDataSource> { MemoTableDBInfoLocalDataSourceImpl(get()) }
    single<MemoDao> { get<AppDatabase>().memoDao() }

    single<AppDatabase> {
        Room.databaseBuilder(
            get(),
            AppDatabase::class.java, Const.DB_NAME
        ).build()
    }
}

