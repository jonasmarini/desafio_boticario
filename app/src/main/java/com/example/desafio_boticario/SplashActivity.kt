package com.example.desafio_boticario

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity


class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val intent = Intent(this@SplashActivity, MainActivity::class.java)
        startActivity(intent)
        finish()

//        Handler(Looper.getMainLooper()).postDelayed({
//            val intent = Intent(this@SplashActivity, LoginActivity::class.java) //TODO VOLTAR ESSE DEPOIS
//            startActivity(intent)
//            finish()
//        }, 3000)
    }

}