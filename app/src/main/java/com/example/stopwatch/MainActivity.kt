package com.example.stopwatch

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.stopwatch.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var isRunning = false
    private var startTime = 0L
    private var elapsedTime = 0L
    private val handler = Handler(Looper.getMainLooper())

    private val updateTimer = object : Runnable {
        override fun run() {
            val now = System.currentTimeMillis()
            val time = now - startTime + elapsedTime
            val minutes = (time / 1000) / 60
            val seconds = (time / 1000) % 60
            val milliseconds = (time % 1000)
            binding.tvTimer.text = String.format("%02d:%02d:%03d", minutes, seconds, milliseconds)

            if (isRunning) {
                handler.postDelayed(this, 10)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnStart.setOnClickListener {
            if (!isRunning) {
                startTime = System.currentTimeMillis()
                handler.post(updateTimer)
                isRunning = true
            }
        }

        binding.btnPause.setOnClickListener {
            if (isRunning) {
                elapsedTime += System.currentTimeMillis() - startTime
                handler.removeCallbacks(updateTimer)
                isRunning = false
            }
        }

        binding.btnReset.setOnClickListener {
            startTime = 0L
            elapsedTime = 0L
            binding.tvTimer.text = "00:00:000"
            if (isRunning) {
                startTime = System.currentTimeMillis()
                handler.post(updateTimer)
            }
        }
    }
}
