package com.stopwatch

import android.app.Service
import android.content.Intent
import android.os.IBinder
import java.util.*

class TimerService: Service() {

    private var timer = Timer()

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        timer.cancel()
        super.onDestroy()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val timeIntent = intent.getDoubleExtra(TIME_EXTRA, 0.00)
        timer.scheduleAtFixedRate(TimeTask(timeIntent), 0 , 1000)
        return START_NOT_STICKY
    }

    private inner class TimeTask(private var time: Double): TimerTask() {

        override fun run() {
             val intent = Intent(TIMER_UPDATED)
            time++
            intent.putExtra(TIME_EXTRA, time)
            sendBroadcast(intent)
        }
    }

    companion object {
        const val TIMER_UPDATED = "timerUpdated"
        const val TIME_EXTRA = "timeExtra"
    }


}