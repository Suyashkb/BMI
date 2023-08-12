package com.example.bmicalculator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.bmicalculator.databinding.ActivitySendOtpBinding
import org.json.JSONObject

class SendOtpActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySendOtpBinding
    private lateinit var requestQueue: RequestQueue
    private val url = "http://3.6.88.40:3001/api/v1/auth/sendOtp"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySendOtpBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val queue = Volley.newRequestQueue(this)

        binding.getOtp.setOnClickListener {
            var pNumber : String = "+91" + binding.inputMobile.text.toString()
            binding.getOtp.visibility = View.GONE
            binding.progressBar.visibility=View.VISIBLE

            requestQueue = Volley.newRequestQueue(this)

            val jsonBody = JSONObject().apply {
                put("number", pNumber)
            }
            val request = JsonObjectRequest(
                Request.Method.POST, url, jsonBody,
                { response ->
                    val status = response.getString("status")
                    if (status=="ok") {
                        var intent = Intent(this,VerifyOtpActivity::class.java)
                        intent.putExtra("PHONE_NUMBER",pNumber)
                        startActivity(intent)
                        finish()
                    }else {
                        binding.getOtp.visibility = View.VISIBLE
                        binding.progressBar.visibility=View.GONE
                        Toast.makeText(this,"Wrong phone NUmber",Toast.LENGTH_SHORT).show()
                    }
                },
                { error ->
                    binding.inputMobile.setText("")
                    binding.progressBar.visibility =View.GONE
                    binding.getOtp.visibility =View.VISIBLE
                    Toast.makeText(this,"something went wrong",Toast.LENGTH_SHORT).show()
                    Log.e("API_ERROR", "Error: ${error.message}")
                }
            )
            requestQueue.add(request)
        }

        }


    }