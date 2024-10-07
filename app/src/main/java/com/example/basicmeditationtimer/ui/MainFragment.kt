package com.example.basicmeditationtimer.ui

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.basicmeditationtimer.databinding.FragmentMainBinding
import java.util.Locale
import kotlin.concurrent.timer

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // Establish view objects
        val timerPicker = binding.npTimer
        val warningPicker = binding.npWarning
        val startButton = binding.btnStart
        val progressBar = binding.progressbar
        val countdown = binding.tvCountdown

        // Customize warning number picker (seconds = warningPicker.value * 30)
        val array = arrayOf("30", "60", "90", "120", "150")
        warningPicker.setDisplayedValues(array)

        // Connect start button to action
        startButton.setOnClickListener {

            // Show hidden views and disable button
            progressBar.visibility = View.VISIBLE
            countdown.visibility = View.VISIBLE
            countdown.text = "${timerPicker.value}:00"
            startButton.isEnabled = false

            // Initialize Timer object
            val timer = object: CountDownTimer(timerPicker.value*60000.toLong(), 1000){
                override fun onTick(millisUntilFinished: Long) {
                    progressBar.progress = 100 - ((timerPicker.value * 60000 - millisUntilFinished.toInt()).toFloat() / (timerPicker.value * 60000).toFloat() * 100).toInt()

                    var minutes = millisUntilFinished / 60000
                    var seconds = (millisUntilFinished % 60000) / 1000
                    countdown.text = String.format("%02d:%02d", minutes, seconds)
                }

                override fun onFinish() {
                    progressBar.visibility = View.INVISIBLE
                    countdown.visibility = View.INVISIBLE
                    startButton.isEnabled = true
                }

            }

            // Start timer
            Log.v("Debug", "Timer value: ${timerPicker.value}, Warning value: ${warningPicker.value}")
            timer.start()

        }

    }

}