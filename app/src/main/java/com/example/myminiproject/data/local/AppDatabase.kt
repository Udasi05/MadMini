package com.example.myminiproject.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myminiproject.data.local.dao.SchemeDao
import com.example.myminiproject.data.local.dao.SupportTicketDao
import com.example.myminiproject.data.local.dao.TransactionDao
import com.example.myminiproject.data.local.dao.UserProfileDao
import com.example.myminiproject.data.local.entities.SchemeEntity
import com.example.myminiproject.data.local.entities.SupportTicketEntity
import com.example.myminiproject.data.local.entities.TransactionEntity
import com.example.myminiproject.data.local.entities.UserProfileEntity

@Database(
    entities = [
        TransactionEntity::class,
        UserProfileEntity::class,
        SupportTicketEntity::class,
        SchemeEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun transactionDao(): TransactionDao
    abstract fun userProfileDao(): UserProfileDao
    abstract fun supportTicketDao(): SupportTicketDao
    abstract fun schemeDao(): SchemeDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "dhansathi_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
