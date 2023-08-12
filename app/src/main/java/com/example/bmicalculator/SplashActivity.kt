package com.example.bmicalculator

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.TextView

class SplashActivity : AppCompatActivity() {
    lateinit var wlcmText :TextView
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        var alpha = AnimationUtils.loadAnimation(this,R.anim.alpha)
        var splashTitle : TextView= findViewById(R.id.splash_title)
        splashTitle.animation = alpha
        var sharedPreferences = getSharedPreferences("BMI_DATA", Context.MODE_PRIVATE)
        var pName = sharedPreferences.getString("NAME",null)
        wlcmText= findViewById(R.id.splash_welcome)
        if (pName!=null){
            var fName= pName.split(" ")[0]
            wlcmText.text="Welcome "+fName+" !!"
            Handler().postDelayed({
                wlcmText.visibility = View.VISIBLE
                wlcmText.animation=alpha
            },1000)
        }

        Handler().postDelayed({
            if (pName!=null){
                startActivity(Intent(this@SplashActivity,MainActivity::class.java))
                finish()
            }else{
                startActivity(Intent(this@SplashActivity,SendOtpActivity::class.java))
                finish()
            }
        },4000)
    }
}