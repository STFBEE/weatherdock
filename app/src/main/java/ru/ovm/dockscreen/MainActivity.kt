package ru.ovm.dockscreen

import android.os.Bundle
import android.view.Window
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var timeTimer: Timer
    private lateinit var weatherTimer: Timer

    private lateinit var api: Api

    private val scope = MainScope()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        api = Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(
                        OkHttpClient.Builder()
                                .addInterceptor(HttpLoggingInterceptor().apply {
                                    level = HttpLoggingInterceptor.Level.BODY
                                })
                                .build()
                )
                .build().create(Api::class.java)

        setContentView(R.layout.activity_main)
        updateTime()
    }

    override fun onResume() {
        super.onResume()
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
            window.insetsController?.hide(WindowInsets.Type.systemBars())
        } else {
            @Suppress("DEPRECATION")
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }

        timeTimer = Timer().apply {
            scheduleAtFixedRate(object : TimerTask() {
                override fun run() {
                    updateTime()
                }
            }, 0, 1000)
        }

        weatherTimer = Timer().apply {
            scheduleAtFixedRate(object : TimerTask() {
                override fun run() {
                    getWeather()
                }
            }, 0, 1000 * 60 * 5)
        }
    }

    override fun onPause() {
        super.onPause()
        timeTimer.cancel()
        weatherTimer.cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }

    private fun updateTime() = runOnUiThread {
        val currentDateTimeString = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
        time_text.text = currentDateTimeString
    }

    private fun getWeather() {
        scope.launch(Dispatchers.IO) {
            val weather = api.getWeather("moscow", BuildConfig.OWM_API_KEY, Locale.getDefault().language)
            withContext(Dispatchers.Main) {
                showWeather(weather)
            }
        }
    }

    private fun showWeather(response: Response?) {
        if (response == null) {
            weather_text.text = ""
            icon.setImageDrawable(null)
        } else {
            val weather = response.weather.first()
            icon.setImageResource(Utils.getIcon(weather.icon))

            var desc = weather.description

            if (desc.contains(" ")) {
                desc = desc.split(" ")[0]
            }

            desc = desc.capitalize(Locale.getDefault())

            weather_text.text = getString(R.string.temp, desc, Utils.toCelsius(response.main.feels_like))
            real_text.text = getString(R.string.real_temp, Utils.toCelsius(response.main.temp))
        }
    }

    companion object {
        const val API_URL = "https://api.openweathermap.org/data/2.5/"
    }
}