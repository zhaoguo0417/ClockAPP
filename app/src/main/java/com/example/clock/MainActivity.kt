package com.example.clock

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import android.app.Activity
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : Activity() {

    private lateinit var tvClock: TextView
    private val mainHandler = Handler(Looper.getMainLooper())
    private val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())

    private val tickRunnable = object : Runnable {
        override fun run() {
            val now = Date()
            tvClock.text = timeFormat.format(now)
            mainHandler.postDelayed(this, 1000L)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tvClock = findViewById(R.id.tvClock)
    }

    override fun onResume() {
        super.onResume()
        mainHandler.post(tickRunnable)
    }

    override fun onPause() {
        super.onPause()
        mainHandler.removeCallbacks(tickRunnable)
    }
}
