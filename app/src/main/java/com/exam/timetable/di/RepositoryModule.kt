package com.exam.timetable.di

import com.exam.timetable.model.repository.timetable.TimeTableInfoRepositoryImpl
import com.exam.timetable.model.repository.memotable.MemoTableInfoRepositoryImpl
import com.exam.timetable.model.repository.detail.DetailDataRepository
import com.exam.timetable.model.repository.detail.DetailDataRepositoryImpl
import com.exam.timetable.model.repository.memo.MemoRepository
import com.exam.timetable.model.repository.memo.MemoRepositoryImpl

import com.exam.timetable.model.repository.search.SearchRepository
import com.exam.timetable.model.repository.search.SearchRepositoryImpl
import com.exam.timetable.model.repository.timetable.MemotableInfoRepository
import com.exam.timetable.model.repository.timetable.TimetableInfoRepository

import org.koin.dsl.module


val repoModule = module {

    single<DetailDataRepository> { DetailDataRepositoryImpl(get()) }
    single<MemoRepository> { MemoRepositoryImpl(get()) }
    single<SearchRepository> { SearchRepositoryImpl(get()) }
    single<MemotableInfoRepository> { MemoTableInfoRepositoryImpl(get()) }
    single<TimetableInfoRepository> { TimeTableInfoRepositoryImpl(get()) }

}
