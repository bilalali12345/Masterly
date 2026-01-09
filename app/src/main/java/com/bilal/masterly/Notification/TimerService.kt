package com.bilal.masterly.Notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.bilal.masterly.MyApplication
import com.bilal.masterly.R
import com.bilal.masterly.Ui_Layer.MainActivity
import com.bilal.masterly.Ui_Layer.Screen
import com.bilal.masterly.repository.TimerRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TimerService : Service() {

    private lateinit var repo: TimerRepository
    private var timerJob: Job? = null

    // Service-scoped coroutine scope so we can cancel all coroutines in onDestroy
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    companion object {
        const val CHANNEL_ID = "timer_channel"
        const val CHANNEL_NAME = "Timer"
        const val NOTIFICATION_ID = 1001

        const val ACTION_START = "com.bilal.masterly.action.START_TIMER"
        const val ACTION_PAUSE = "com.bilal.masterly.action.PAUSE_TIMER"
        const val ACTION_STOP = "com.bilal.masterly.action.STOP_TIMER"
        const val ACTION_CONTINUE = "com.bilal.masterly.action.CONTINUE_TIMER"

        // Intent extra name
        const val EXTRA_SKILL_ID = "extra_skill_id"
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannelIfNeeded()
        repo = (application as MyApplication).timerRepository
    }

    override fun onDestroy() {
        super.onDestroy()
        timerJob?.cancel()
        serviceScope.cancel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.action
        // read long extra safely; -1 means "not provided"
        val skillIdFromIntent = intent?.getLongExtra(EXTRA_SKILL_ID, -1L)?.takeIf { it != -1L }

        when (action) {
            ACTION_START -> handleStart(skillIdFromIntent)
            ACTION_PAUSE -> handlePause()
            ACTION_STOP -> handleStop()
            ACTION_CONTINUE -> handleContinue()
        }
        return START_STICKY
    }

    private fun handleStart(intentSkillId: Long?) {
        // We must call startForeground quickly. If the repo already has currentSkill,
        // build the notification immediately. Otherwise load the skill then start foreground.
        createNotificationChannelIfNeeded()

        if (repo.currentSkill.value != null) {
            // repo already has skill (UI likely set it). start foreground immediately.
            val notification = buildNotification(repo.elapsedSeconds.value, true)
            startForeground(NOTIFICATION_ID, notification)
            // then start the timer loop
            if (!repo.isRunning.value) {
                repo.setRunning(true)
                repo.startTimestampMillis =
                    System.currentTimeMillis() - (repo.elapsedSeconds.value * 1000L)
                startTimerLoop()
            }
            return
        }

        // repo has no skill; try to load once from DB if intent provides id.
        if (intentSkillId != null) {
            // load skill on Main then call startForeground (startForeground must be called on main thread)
            serviceScope.launch(Dispatchers.Main) {
                try {
                    repo.loadCurrentSkillOnce(intentSkillId) // suspend; populates repo.currentSkill
                } catch (t: Throwable) {
                    // ignore or log; still build a fallback notification
                }
                val notification = buildNotification(repo.elapsedSeconds.value, true)
                startForeground(NOTIFICATION_ID, notification)

                if (!repo.isRunning.value) {
                    repo.setRunning(true)
                    repo.startTimestampMillis =
                        System.currentTimeMillis() - (repo.elapsedSeconds.value * 1000L)
                    startTimerLoop()
                }
            }
        } else {
            // No skill id provided and repo empty — show fallback notification and continue.
            val notification = buildNotification(repo.elapsedSeconds.value, true)
            startForeground(NOTIFICATION_ID, notification)
            if (!repo.isRunning.value) {
                repo.setRunning(true)
                repo.startTimestampMillis =
                    System.currentTimeMillis() - (repo.elapsedSeconds.value * 1000L)
                startTimerLoop()
            }
        }
    }

    private fun startTimerLoop() {
        timerJob?.cancel()
        timerJob = serviceScope.launch {
            while (isActive && repo.isRunning.value) {
                val elapsed = ((System.currentTimeMillis() - (repo.startTimestampMillis
                    ?: System.currentTimeMillis())) / 1000L)
                repo.updateElapsed(elapsed)

                // update notification (NotificationManager.notify is safe from background thread)
                val notif = buildNotification(elapsed, true)
                (getSystemService(NOTIFICATION_SERVICE) as NotificationManager).notify(
                    NOTIFICATION_ID,
                    notif
                )

                delay(1000L)
            }
        }
    }

    private fun handleContinue() {
        if (repo.isRunning.value) return
        repo.setRunning(true)
        repo.startTimestampMillis = System.currentTimeMillis() - (repo.elapsedSeconds.value * 1000L)
        startTimerLoop()
        // update notification immediately on main thread
        serviceScope.launch(Dispatchers.Main) {
            val notif = buildNotification(repo.elapsedSeconds.value, true)
            (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).notify(
                NOTIFICATION_ID,
                notif
            )
        }
    }

    private fun handlePause() {
        repo.setRunning(false)
        timerJob?.cancel()
        serviceScope.launch(Dispatchers.Main) {
            val notif = buildNotification(repo.elapsedSeconds.value, false)
            (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).notify(
                NOTIFICATION_ID,
                notif
            )
        }
    }

    private fun handleStop() {
        // stop timer loop immediately
        repo.setRunning(false)
        timerJob?.cancel()

        // capture elapsed & current skill BEFORE resetting
        val elapsed = repo.elapsedSeconds.value
        val skill = repo.currentSkill.value

        // persist the change then reset repo and stop service
        serviceScope.launch(Dispatchers.IO) {
            try {
                if (skill != null && elapsed > 0L) {
                    // convert seconds to hours to match your Skill.hoursCompleted (Int)
                    val addedHours = (elapsed / 3600).toInt()

                    // if you want to add fractional hours store differently; here we add integer hours
                    val updated = skill.copy(hoursCompleted = skill.hoursCompleted + addedHours)

                    repo.updateSkill(updated) // suspend DB write
                }
            } catch (t: Throwable) {
                // log or handle error - don't crash service
            } finally {
                // reset repo state on IO thread (safe) and stop service on main thread
                repo.updateElapsed(0L)
                repo.setRunning(false)

                // stopForeground / stopSelf must be on main thread
                withContext(Dispatchers.Main) {
                    stopForeground(true)
                    stopSelf()
                }
            }
        }
    }


    private fun buildNotification(elapsedSeconds: Long, running: Boolean): Notification {
        val content = formatTime(elapsedSeconds)

        // prefer Long extra for skill id
        val currentSkill = repo.currentSkill.value
        val skillIdLong = currentSkill?.id ?: -1L

        val deepIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("navigate_to", Screen.Timer) // your existing convention
            putExtra(EXTRA_SKILL_ID, skillIdLong) // put long extra
        }

        val pendingOpen = TaskStackBuilder.create(this).run {
            addNextIntentWithParentStack(deepIntent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        }

        val pausePending = PendingIntent.getService(
            this,
            1,
            Intent(this, TimerService::class.java).apply { action = ACTION_PAUSE },
            PendingIntent.FLAG_IMMUTABLE
        )
        val stopPending = PendingIntent.getService(
            this,
            2,
            Intent(this, TimerService::class.java).apply { action = ACTION_STOP },
            PendingIntent.FLAG_IMMUTABLE
        )
        val continuePending = PendingIntent.getService(
            this,
            3,
            Intent(this, TimerService::class.java).apply { action = ACTION_CONTINUE },
            PendingIntent.FLAG_IMMUTABLE
        )

        val title =
            if (currentSkill != null) "Timer running for ${currentSkill.name}" else "Timer running"

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(content)
            .setOngoing(running)
            .setContentIntent(pendingOpen)
            .addAction(R.drawable.pause_24px, "Pause", pausePending)
            .addAction(R.drawable.stop_24px, "Stop", stopPending)
            .addAction(R.drawable.play_arrow_24px, "Continue", continuePending)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
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
