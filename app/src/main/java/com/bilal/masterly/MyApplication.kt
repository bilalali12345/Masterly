package com.bilal.masterly

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.bilal.masterly.data.AppDatabase
import com.bilal.masterly.repository.SkillRepository
import com.bilal.masterly.repository.TimerRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class MyApplication : Application() {


    private val database by lazy { AppDatabase.getDatabase(this) }

    val skillRepository by lazy { SkillRepository(database.skillDao()) }
    val timerRepository by lazy { TimerRepository(database.skillDao() , CoroutineScope(SupervisorJob() )) }

    override fun onCreate() {
        super.onCreate()

        val channel = NotificationChannel(
            "TIMER_NOTIFICATION",
            "Timer Notification",
            NotificationManager.IMPORTANCE_HIGH
        )
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
        Thread.setDefaultUncaughtExceptionHandler(GlobalExceptionHandler())



    }
}