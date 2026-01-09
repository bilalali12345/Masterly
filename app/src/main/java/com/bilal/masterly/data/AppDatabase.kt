package com.bilal.masterly.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.bilal.masterly.Domain_Layer.Skill

@Database(entities = [SkillEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun skillDao(): SkillDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "masterly_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
