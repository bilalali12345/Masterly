package com.bilal.masterly

import android.util.Log
import kotlin.system.exitProcess

class GlobalExceptionHandler : Thread.UncaughtExceptionHandler {
    override fun uncaughtException(thread: Thread, throwable: Throwable) {
        // Logthe exception to Logcat with a specific tag
        Log.e("AppCrash", "Uncaught exception: ", throwable)

        // Here you can also save the crash report to a file,
        // or send it to aremote server.

        // It's important to re-throw the exception or kill the process
        // to ensure the app closes correctly.
        android.os.Process.killProcess(android.os.Process.myPid())
        exitProcess(10)
    }
}