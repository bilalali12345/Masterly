package com.bilal.masterly.Notification

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.bilal.masterly.R
import com.bilal.masterly.repository.TimerRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class TimerService : Service() {

    companion object {
        const val CHANNEL_ID = "timer_channel"
        const val CHANNEL_NAME = "Timer"
        const val NOTIFICATION_ID = 1001

        const val ACTION_START = "com.bilal.masterly.action.START_TIMER"
        const val ACTION_PAUSE = "com.bilal.masterly.action.PAUSE_TIMER"
        const val ACTION_STOP = "com.bilal.masterly.action.STOP_TIMER"
        lateinit var timerJob: Job
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannelIfNeeded()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> handleStart()
            ACTION_PAUSE -> handlePause()
            ACTION_STOP -> handleStop()
        }
        return START_STICKY
    }

    private fun handleStart() {
        createNotificationChannelIfNeeded()
        val notification = buildNotification(TimerRepository.elapsedSeconds.value, true)
        startForeground(NOTIFICATION_ID, notification)

        // If already running, ignore
        if (TimerRepository.isRunning.value) return

        TimerRepository.setRunning(true)
        TimerRepository.startTimestampMillis =
            System.currentTimeMillis() - (TimerRepository.elapsedSeconds.value * 1000L)

        timerJob = CoroutineScope(Dispatchers.Default).launch {
            while (isActive && TimerRepository.isRunning.value) {
                val elapsed = ((System.currentTimeMillis() - (TimerRepository.startTimestampMillis
                    ?: System.currentTimeMillis())) / 1000L)
                TimerRepository.updateElapsed(elapsed)
                // Update notification with new time:
                val notif = buildNotification(elapsed, true)
                (getSystemService(NOTIFICATION_SERVICE) as NotificationManager)
                    .notify(NOTIFICATION_ID, notif)
                delay(1000L)
            }
        }
    }

    private fun handlePause() {
        TimerRepository.setRunning(false)
        timerJob?.cancel()
        // keep elapsedSeconds as-is; do NOT stopForeground so user still sees notification if wanted
        val notif = buildNotification(TimerRepository.elapsedSeconds.value, false)
        (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).notify(
            NOTIFICATION_ID,
            notif
        )
    }

    private fun handleStop() {
        TimerRepository.setRunning(false)
        timerJob?.cancel()
        stopForeground(true)
        stopSelf()
    }

    private fun buildNotification(elapsedSeconds: Long, running: Boolean): Notification {
        val content = formatTime(elapsedSeconds)
        // Add PendingIntents for pause/stop actions
        val pauseIntent = Intent(this, TimerService::class.java).apply { action = ACTION_PAUSE }
        val pausePending =
            PendingIntent.getService(this, 1, pauseIntent, PendingIntent.FLAG_IMMUTABLE)
        val stopIntent = Intent(this, TimerService::class.java).apply { action = ACTION_STOP }
        val stopPending =
            PendingIntent.getService(this, 2, stopIntent, PendingIntent.FLAG_IMMUTABLE)

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Timer running")
            .setContentText(content)
            .setOngoing(running)
            .addAction(R.drawable.pause_24px, "Pause", pausePending)
            .addAction(R.drawable.stop_24px, "Stop", stopPending)
            .setPriority(NotificationCompat.PRIORITY_LOW)

        return builder.build()
    }

    private fun createNotificationChannelIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val existing = nm.getNotificationChannel(CHANNEL_ID)
            if (existing == null) {
                val channel = NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_LOW
                ).apply {
                    description = "Timer foreground notification"
                }
                nm.createNotificationChannel(channel)
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun formatTime(seconds: Long): String {
        val hours = seconds / 3600
        val minutes = (seconds % 3600) / 60
        val secs = seconds % 60
        return "%02d:%02d:%02d".format(hours, minutes, secs)
    }

}
