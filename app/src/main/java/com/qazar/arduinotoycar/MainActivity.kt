package com.qazar.arduinotoycar

import android.annotation.SuppressLint
import android.content.Intent
import android.content.Intent.*
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.MotionEvent
import android.widget.Button
import android.widget.Toast
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import okhttp3.*
import java.io.IOException

private val client = OkHttpClient()
class MainActivity : AppCompatActivity() {
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        MobileAds.initialize(this) {}
        val mAdView = findViewById<AdView>(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
        val btnForward = findViewById<Button>(R.id.btnForward)
        btnForward.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    // Обработка события начала нажатия
                    handleEvents(Directions.FORWARD, "Down")
                    true
                }
                MotionEvent.ACTION_UP -> {
                    // Обработка события отпускания
                    handleEvents(Directions.FORWARD,"Up")
                    true
                }
                else -> false
            }
        }

        val btnLeft = findViewById<Button>(R.id.btnLeft)
        btnLeft.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    // Обработка события начала нажатия
                    handleEvents(Directions.LEFT, "Down")
                    true
                }
                MotionEvent.ACTION_UP -> {
                    // Обработка события отпускания
                    handleEvents(Directions.LEFT, "Up")
                    true
                }
                else -> false
            }
        }
        val btnRight = findViewById<Button>(R.id.btnRight)
        btnRight.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    // Обработка события начала нажатия
                    handleEvents(Directions.RIGHT, "Down")
                    true
                }
                MotionEvent.ACTION_UP -> {
                    // Обработка события отпускания
                    handleEvents(Directions.RIGHT, "Up")
                    true
                }
                else -> false
            }
        }
        val btnBack = findViewById<Button>(R.id.btnBack)
        btnBack.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    // Обработка события начала нажатия
                    handleEvents(Directions.BACK, "Down")
                    true
                }
                MotionEvent.ACTION_UP -> {
                    // Обработка события отпускания
                    handleEvents(Directions.BACK,"Up")
                    true
                }
                else -> false
            }
        }
    }

    private fun handleEvents(direction: Directions, state : String){
        when (direction){
            Directions.FORWARD ->{
                run("forwardBtn$state")
            }
            Directions.LEFT -> {
                run("leftBtn$state")
            }
            Directions.RIGHT -> {
                run("rightBtn$state")
            }
            Directions.BACK -> {
                run("backBtn$state")
            }
        }

    }
    private fun run(dir : String) {
        val request = Request.Builder()
            .url("http://192.168.4.1:80/$dir")
            .build()
        //Toast.makeText(this, "http://192.168.4.1:80/$dir", Toast.LENGTH_LONG).show()
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                // Обработка успешного ответа
                if (response.isSuccessful) {
                    println("Успешный ответ: ${response.body?.string()}")
                } else {
                    println("Неуспешный ответ: ${response.code}")
                }
            }
            override fun onFailure(call: Call, e: IOException) {
                // Обработка ошибки
                println("Ошибка при выполнении запроса: ${e.message}")
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater : MenuInflater = menuInflater
        inflater.inflate(R.menu.menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId){
            R.id.btn_preview -> {
                val intent : Intent = Intent().apply {
                    action = Intent.ACTION_VIEW
                    data = Uri.parse("https://github.com/kz-kvazar/ArduinoToyCar/blob/master/ToyCar.ino")
                }
                startActivity(intent)
                true
            }
            R.id.btn_share -> {
                //TODO add copy in buffer and share action
                val intent = Intent().apply {
                    action = ACTION_SEND
                    putExtra(EXTRA_TEXT, "https://github.com/kz-kvazar/ArduinoToyCar/blob/master/ToyCar.ino")
                    type = "text/plain"
                }
                startActivity(Intent.createChooser(intent, "Share a link to a sketch with:"))
                true
            }
            else -> {super.onOptionsItemSelected(item)}
        }
    }
}