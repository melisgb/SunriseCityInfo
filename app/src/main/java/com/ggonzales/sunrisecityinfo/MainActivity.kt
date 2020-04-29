package com.ggonzales.sunrisecityinfo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        submitButton.setOnClickListener {
            getSunset(it)
        }
    }

    protected fun getSunset(view: View){
        var city = cityEditText.text.toString()
        val url = "https://weather-ydn-yql.media.yahoo.com/forecastrss?location=$city"
//        val url = "api.openweathermap.org/data/2.5/weather?q=$city"
        cityResultTextView.text = url
    }
}
