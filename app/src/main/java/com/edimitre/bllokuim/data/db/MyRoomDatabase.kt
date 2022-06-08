package com.edimitre.bllokuim.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.edimitre.bllokuim.data.dao.*
import com.edimitre.bllokuim.data.model.*


@Database(
    entities = [MainUser::class,Description::class,
        Expense::class,MonthlyIncome::class,
        MonthlyIncomeType::class,Reminder::class], version = 1, exportSchema = false
)
abstract class MyRoomDatabase : RoomDatabase() {

    abstract fun getUserDao(): MainUserDao
    abstract fun getDescriptionDao(): DescriptionDao
    abstract fun getExpenseDao(): ExpenseDao
    abstract fun getMonthlyIncomeDao(): MonthlyIncomeDao
    abstract fun getReminderIncomeDao(): ReminderDao

    companion object {

        private var INSTANCE: MyRoomDatabase? = null

        fun getInstance(context: Context): MyRoomDatabase {

            var instance = INSTANCE

            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    MyRoomDatabase::class.java,
                    "BllokuDatabase.db"
                )
                    .fallbackToDestructiveMigration()
                    .setJournalMode(JournalMode.TRUNCATE)
                    .build()
                INSTANCE = instance
            }
            return instance

        }
    }

}