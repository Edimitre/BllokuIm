package com.edimitre.bllokuim


import android.app.Application
import com.edimitre.bllokuim.data.dao.DescriptionDao
import com.edimitre.bllokuim.data.dao.ExpenseDao

import com.edimitre.bllokuim.data.dao.MainUserDao
import com.edimitre.bllokuim.data.dao.MonthlyIncomeDao
import com.edimitre.bllokuim.data.db.MyRoomDatabase

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Singleton
    @Provides
    fun getRoomDb(context: Application): MyRoomDatabase {

        return MyRoomDatabase.getInstance(context)

    }


    @Singleton
    @Provides
    fun getUserDao(rDb: MyRoomDatabase): MainUserDao {
        return rDb.getUserDao()
    }

    @Singleton
    @Provides
    fun getDescriptionDao(rDb: MyRoomDatabase): DescriptionDao {
        return rDb.getDescriptionDao()
    }

    @Singleton
    @Provides
    fun getExpenseDao(rDb: MyRoomDatabase): ExpenseDao {
        return rDb.getExpenseDao()
    }

    @Singleton
    @Provides
    fun getMonthlyIncomeDao(rDb: MyRoomDatabase): MonthlyIncomeDao {
        return rDb.getMonthlyIncomeDao()
    }

}