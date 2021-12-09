package com.stopwatch

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {

    private lateinit var serviceIntent: Intent
    private var timerStarted = false
    private var time = 0.0

    private var updateTime: BroadcastReceiver = object: BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            time = intent.getDoubleExtra(TimerService.TIME_EXTRA, 0.0)
            tvText.text =  getTimeInString(time)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        serviceIntent = Intent(applicationContext, TimerService::class.java)
        registerReceiver(updateTime, IntentFilter(TimerService.TIMER_UPDATED))


        btnStartStop.setOnClickListener{
            startOrStopTimer()
        }

        btnReset.setOnClickListener {
            resetTimer()
        }
    }

    private fun startOrStopTimer() {
        if (timerStarted)
            stopTimer()
        else
            startTimer()
    }

    private fun startTimer() {
        serviceIntent.putExtra(TimerService.TIME_EXTRA, time)
        startService(serviceIntent)
        btnStartStop.text = getString(R.string.stop)
        btnStartStop.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_pause_24, 0, 0, 0)
        timerStarted = true
    }

    private fun stopTimer() {
        stopService(serviceIntent)
        btnStartStop.text = getString(R.string.start)
        btnStartStop.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_play_arrow_24, 0, 0, 0);
        timerStarted = false
    }

    private fun resetTimer() {
        stopTimer()
        time = 0.0
        tvText.text = getTimeInString(time)
    }

    private fun getTimeInString(time: Double): String {
        val result = time.roundToInt()
        val hours = result % 86400 / 3600
        val minutes = result % 86400  % 3600 / 60
        val seconds = result % 86400 % 3600 % 60
        return makeTimeString(hours, minutes, seconds)
    }

    private fun makeTimeString(hours: Int, minutes: Int, seconds: Int): String {
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }
}