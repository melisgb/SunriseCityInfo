package com.ggonzales.sunrisecityinfo

import android.os.AsyncTask
import android.os.Bundle
import android.util.Base64InputStream
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

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
        val url = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22$city%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys"
//        "https://weather-ydn-yql.media.yahoo.com/forecastrss?location=$city&format=json"
//        val url = "api.openweathermap.org/data/2.5/weather?q=$city"
        cityResultTextView.text = url
        MyAsyncTask().execute(url)
    }

    inner class MyAsyncTask : AsyncTask<String, String, String> {
        constructor(){

        }

        override fun onPreExecute() {
            //before task started
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
            return ""
        }

        override fun onProgressUpdate(vararg values: String?) {
            try {
                var json = JSONObject(values[0])
                //steps to read each element inside a JSON response
                val query = json.getJSONObject("query")
                val results = query.getJSONObject("results")
                val channel = results.getJSONObject("channel")
                val astronomy = channel.getJSONObject("astronomy")
                val sunrise = astronomy.getString("sunrise")

                cityResultTextView.text = sunrise

            }catch (ex: Exception){
                Log.d("Exception error", ex.message)
            }
        }
        override fun onPostExecute(result : String) {
            //after the task is done
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


