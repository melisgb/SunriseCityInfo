package com.ggonzales.sunrisecityinfo

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Base64InputStream
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*

class MainActivity : AppCompatActivity() {
    var latExtra : Double? = null
    var longExtra : Double? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
            submitButton.setOnClickListener {
                getSunset(it)
            }

            viewMapButton.setOnClickListener {
                val intent = Intent(this, ViewLocation::class.java)
                intent.putExtra("latitud", latExtra)
                intent.putExtra("longitud", longExtra)
                startActivity(intent)
            }
    }
      //info from API : https://openweathermap.org/current#name
    protected fun getSunset(view: View){
        var city = cityEditText.text.toString()
        val api_key = "bb9e57a4b8922a20495b519ddb81ace3"
        val url = "https://api.openweathermap.org/data/2.5/weather?q=$city&APPID=$api_key"
        MyAsyncTask().execute(url)
    }

    inner class MyAsyncTask : AsyncTask<String, String, String>() {

        override fun onPreExecute() {
            //before task started
            super.onPreExecute()
        }
        override fun doInBackground(vararg p0: String?): String {
            //http call
            try {
                val url = URL(p0[0])
                val urlConnect = url.openConnection() as HttpURLConnection
                urlConnect.connectTimeout = 7000
                var inString = convertStreamToString(urlConnect.inputStream)
                //this function will publish the progress to the UI
                publishProgress(inString)
            }catch (ex: Exception){
                Log.d("Exception error", ex.message)
            }
            return " "
        }

        override fun onProgressUpdate(vararg values: String?) {
            try {
                var json = JSONObject(values[0])
                //steps to read each element inside a JSON response
                val sys = json.getJSONObject("sys")
                val sunrise = sys.getLong("sunrise")
                val sunset = sys.getLong("sunset")
                val country = sys.getString("country")
                val cityName = json.getString("name")
                val timezone = json.getLong("timezone")
                val coord = json.getJSONObject("coord")
                val longitude = coord.getDouble("lon")
                val latitude = coord.getDouble("lat")
                latExtra = latitude
                longExtra = longitude

                val sunriseDate = LocalDateTime.ofEpochSecond(sunrise+timezone, 0, ZoneOffset.UTC).toLocalTime()
                val sunSetDate = LocalDateTime.ofEpochSecond(sunset+timezone, 0, ZoneOffset.UTC).toLocalTime()
                val formatter = DateTimeFormatter.ISO_LOCAL_TIME
                val sunriseTimeF = formatter.format(sunriseDate)
                val sunsetTimeF = formatter.format(sunSetDate)
                cityResultTextView.setText("$cityName, $country:")
                citySunriseResultTextView.text = "Sunrise Time $sunriseTimeF"
                citySunsetResultTextView.text = "Sunset Time $sunsetTimeF"



            }catch (ex: Exception){
                Log.d("Exception error", ex.message)
            }
        }
        override fun onPostExecute(result : String?) {
            //after the task is done
            super.onPostExecute(result)
        }
    }

    fun convertStreamToString(inputStrm: InputStream) : String{
        val bufferReader = BufferedReader(InputStreamReader(inputStrm))
        var line: String
        var allString :String = ""

        try{
            do {
                line = bufferReader.readLine()
                if(line!=null){
                    allString+=line
                }
            }
            while(line != null)
            inputStrm.close()
        }catch (ex:Exception){
            Log.d("Exception error", ex.message)
        }
        return allString
    }
}


