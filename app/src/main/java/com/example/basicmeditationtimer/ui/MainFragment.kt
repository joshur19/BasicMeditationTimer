package com.example.basicmeditationtimer.ui

import android.content.res.ColorStateList
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.basicmeditationtimer.R
import com.example.basicmeditationtimer.databinding.FragmentMainBinding
import java.util.Locale

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private lateinit var mediaplayer: MediaPlayer

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

            // Disable start button
            startButton.isEnabled = false
            startButton.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.grey))

            // Reveal progress bar and countdown
            progressBar.visibility = View.VISIBLE
            countdown.visibility = View.VISIBLE

            // Get ding configuration in ms
            val dingDelay = warningPicker.value * 30 * 1000     // value * 30 for seconds then * 1000 for ms
            var dingplayed = false

            // Initialize Timer object
            val timer = object: CountDownTimer(timerPicker.value*60000.toLong(), 1000){
                override fun onTick(millisUntilFinished: Long) {

                    // Progress bar logic
                    progressBar.progress = 100 - ((timerPicker.value * 60000 - millisUntilFinished.toInt()).toFloat() / (timerPicker.value * 60000).toFloat() * 100).toInt()

                    // Countdown timer
                    val minutes = millisUntilFinished / 60000
                    val seconds = (millisUntilFinished % 60000) / 1000
                    countdown.text = String.format(Locale.US, "%02d:%02d", minutes, seconds)

                    // Audio
                    if (millisUntilFinished < dingDelay && !dingplayed){
                        mediaplayer = MediaPlayer.create(requireContext(), R.raw.ding1)
                        mediaplayer.start()
                        dingplayed = true
                    }

                }

                override fun onFinish() {

                    // Hide progress bar and countdown
                    progressBar.visibility = View.INVISIBLE
                    countdown.visibility = View.INVISIBLE

                    // Enable start button
                    startButton.isEnabled = true
                    startButton.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.colorAccent))

                    // Audio
                    mediaplayer = MediaPlayer.create(requireContext(), R.raw.ding2)
                    mediaplayer.start()
                    mediaplayer.setOnCompletionListener {
                        mediaplayer.release()
                    }
                }

            }

            // Start timer
            timer.start()

            // Click on countdown to cancel timer
            countdown.setOnClickListener {
                timer.cancel()
                progressBar.visibility = View.INVISIBLE
                countdown.visibility = View.INVISIBLE
                startButton.isEnabled = true
                startButton.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.colorAccent))

                if(this::mediaplayer.isInitialized) {
                    mediaplayer.release()
                }
            }

        }

    }

    override fun onDestroy() {
        if(this::mediaplayer.isInitialized) {
            mediaplayer.release()
        }

        super.onDestroy()
    }

}