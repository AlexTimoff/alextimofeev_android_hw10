package com.example.alextimofeev_android_hw10

import android.os.Bundle
import android.os.CountDownTimer
import android.widget.SeekBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.alextimofeev_android_hw10.databinding.ActivityMainBinding

//Используем константы
const val IS_TIME_RUN = "isTimeRun"
const val TIME_END = "timeEnd"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var timer: CountDownTimer? = null
    private var isTimeRun = false
    private var timeEnd: Long = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Формулируем работу ползунка
        binding.seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.showProgress.text = "$progress"
                binding.allProgress.progress = progress
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        //Задаем работу кнопки старт
        binding.startButton.setOnClickListener {

        //Если таймер запущен, seekbar становится недоуступным. Идет отсчет времени.
            if (!isTimeRun) {
                binding.seekbar.isEnabled = false

                startCount(binding.seekbar.progress * 1000L)

            } else {
                timer?.cancel()
                isTimeRun = false
                binding.seekbar.isEnabled = true
            }

        }
        if (savedInstanceState != null) {
            isTimeRun = savedInstanceState.getBoolean(IS_TIME_RUN, false)
            if (isTimeRun) {
                timeEnd = savedInstanceState.getLong(TIME_END)
                startCount(timeEnd)
            }
        }
    }

    //Создаем функцию отсчета
    private fun startCount(time: Long) {
        timer = object : CountDownTimer(time, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                timeEnd = millisUntilFinished
                val nextCountdown = millisUntilFinished / 1000
                binding.showProgress.text = "$nextCountdown"
                binding.allProgress.progress = nextCountdown.toInt()
            }

            override fun onFinish() {
                timer?.cancel()
                isTimeRun = false
                binding.seekbar.isEnabled = true
            }
        }.start()
        isTimeRun = true
        binding.seekbar.isEnabled = false
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(IS_TIME_RUN, isTimeRun)
        outState.putLong(TIME_END, timeEnd)
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
    }

}