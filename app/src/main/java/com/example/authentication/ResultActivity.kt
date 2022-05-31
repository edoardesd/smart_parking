package com.example.authentication

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.authentication.data.GoogleAPIService
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_result.*
import okhttp3.*
import java.io.IOException
import kotlinx.android.synthetic.main.activity_result.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

import java.util.Collections.list


class ResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        val apiKey = getString(R.string.googleMapKey)

        val room_name = intent.getStringExtra("room")
        val room_latitude = intent.getDoubleExtra("latitude", 0.0)
        val room_longitude = intent.getDoubleExtra("longitude", 0.0)
        val hour = intent.getIntExtra("hour", 0)
        val minute = intent.getIntExtra("minute", 0)
        val mode = "driving"

//        runDirectionsApi(46.79542968182268, 9.8245288366505,
//            room_latitude, room_longitude,
//            mode,
//            apiKey)

        val apiService = GoogleAPIService()

        GlobalScope.launch(Dispatchers.Main) {
            val currentDirectionResponse = apiService?.getGoogleDirection("46.79542968182268,9.8245288366505", "45.47811143966341,9.23460752183241", "false", "metrics", "driving")
                ?.await()
//            currentDirectionResponse?.rows.first().elements.first().duration.text
            tv_expected_result_car.text = currentDirectionResponse?.rows?.first()?.elements?.first()?.duration?.text
        }

        Log.d(TAG, "Second page: $room_name \n $room_latitude \n $room_longitude \n $hour \n $minute")
        tv_room_selection.setText(room_name).toString()
//        tv_expected_result_car.setText()

    }

}

fun runDirectionsApi(ori_lat: Double, ori_lon: Double, dest_lat: Double, dest_lon: Double, myMode: String, myKey : String) {
//    var url = String.format("https://maps.googleapis.com/maps/api/distancematrix/json?origins=%s,%s" +
//            "&destinations=%s,%s" +
//            "&sensor=false" +
//            "&units=metric" +
//            "&mode=%s" +
//            "&key=%s",
//        ori_lat, ori_lon,
//        dest_lat, dest_lon,
//        myMode,
//        myKey)

    var url = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=46.79542968182268,9.8245288366505&destinations=45.47811143966341,9.23460752183241&sensor=false&units=metric&mode=driving&key=AIzaSyBJYkizjyo1iqL6Flk5kSv8zypy3A0GG5s"
    Log.d(TAG, "Url Request: $url")
    val formBody = FormBody.Builder()
        .build()

    var request = Request.Builder().url(url)
        .post(formBody)
        .build()

    var client = OkHttpClient();
    client.newCall(request).enqueue(object : Callback {
        override fun onResponse(call: Call, response: Response) {
            if (!response.isSuccessful) throw IOException("Unexpected code $response")
//            response.body?.string()?.let { Log.d(TAG, it) }
            val body = response.body?.string()
//            val gson = GsonBuilder().create()
//            val directions_results = Gson().fromJson(body, GResponseModel::class.java)
            val convertedObject: JsonObject = Gson().fromJson(body, JsonObject::class.java)
            Log.d(TAG, "body $convertedObject")

            val rows = convertedObject.get("rows")
            val jsonElements: JsonObject = Gson().fromJson(rows.toString().drop(1).dropLast(1), JsonObject::class.java)
            val responseJson : JsonObject = Gson().fromJson(jsonElements.get("elements").toString().drop(1).dropLast(1), JsonObject::class.java)
            Log.d(TAG, "JSON: $responseJson")

            val response_final = Gson().fromJson<GResponseModel>(responseJson, GResponseModel::class.java)
            Log.d(TAG, "final $response_final")
            val duration = response_final.duration?.text
            Log.d(TAG, "Duration: $duration")

            val textView = R.id.tv_expected_result_car


//            val elements = rows.getAsJsonArray("elements")
//            val jsonArray = JSONTokener(response.body?.string()).nextValue() as JSONArray
//            Log.d(TAG, "json array $jsonArray")
//            for (i in 0 until jsonArray.length()) {
//                // ID
//                val rows = jsonArray.getJSONObject(i).getString("rows")
//                Log.d(TAG, "ROWS: $rows")
//
//                val elements = jsonArray.getJSONObject(i).getString("elements")
//                Log.d(TAG, "elements $elements")
//            }
//            val jsonObject = JSONTokener(response.body?.string()).nextValue() as JSONObject
//            Log.d(TAG,"json $jsonObject")

//            var rows =  jsonObject.getString("rows")
//            var json_response = JSONObject(response.body?.string())
//            var sessionArray: JSONArray = json_response.optJSONArray("rows")
//            Log.d(TAG, "Session $sessionArray")
//            var first_object = sessionArray[0]
////            var elements : JSONArray = first_object["elements"].optJSONArray("")
//
//            var elements = json_response["rows"]
//
//            Log.d(TAG, elements.toString())
//            Log.d(TAG, first_object.toString())

        }

        override fun onFailure(call: Call, e: IOException) {
            Log.d(TAG, e.message.toString())
        }
    })
}
