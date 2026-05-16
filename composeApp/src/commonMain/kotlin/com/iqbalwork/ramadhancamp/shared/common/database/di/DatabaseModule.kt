package com.iqbalwork.ramadhancamp.shared.common.database.di

import com.iqbalwork.ramadhancamp.shared.common.database.AppDatabase
import com.iqbalwork.ramadhancamp.shared.common.database.getDatabaseBuilder
import org.koin.dsl.module

val databaseModule = module {
    single<AppDatabase> { getDatabaseBuilder().fallbackToDestructiveMigration(true).build() }
}
