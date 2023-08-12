package com.example.bmicalculator

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.bmicalculator.databinding.ActivitySendOtpBinding
import com.example.bmicalculator.databinding.ActivityVerifyOtpBinding
import org.json.JSONObject

class VerifyOtpActivity : AppCompatActivity() {
    private lateinit var binding : ActivityVerifyOtpBinding
    private lateinit var requestQueue: RequestQueue
    private val url = "http://3.6.88.40:3001/api/v1/auth/verifyOtp"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerifyOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fun getOtp():String{
            var otp =  binding.inputCode1.text.toString() +binding.inputCode2.text.toString()+binding.inputCode3.text.toString()+binding.inputCode4.text.toString()+binding.inputCode5.text.toString()+binding.inputCode6.text.toString()
        return otp
        }

        requestQueue = Volley.newRequestQueue(this)
        var pNumber = intent.getStringExtra("PHONE_NUMBER") as String

        Log.d("otp",getOtp())

        val sharedPreference =  getSharedPreferences("BMI_DATA", Context.MODE_PRIVATE)
        var editor = sharedPreference.edit()

        val inputCodes = arrayOf(binding.inputCode1, binding.inputCode2, binding.inputCode3, binding.inputCode4, binding.inputCode5, binding.inputCode6)
        for (i in inputCodes.indices) {
            val currentEditText = inputCodes[i]

            currentEditText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s?.length == 1) {
                        if (i < inputCodes.size - 1) {
                            currentEditText.removeTextChangedListener(this)
                            currentEditText.setText(s)
                            inputCodes[i + 1].requestFocus()
                            currentEditText.addTextChangedListener(this)
                        } else {
                            // Last input code reached, perform desired action (e.g., verification)
                        }
                    } else if (s?.isEmpty() == true && i > 0) {
                        currentEditText.removeTextChangedListener(this)
                        inputCodes[i - 1].requestFocus()
                        currentEditText.addTextChangedListener(this)
                    }
                }

                override fun afterTextChanged(s: Editable?) {}
            })
        }


        binding.buttonVerify.setOnClickListener {
            var otp = getOtp()
            val jsonBody = JSONObject().apply {
                put("number", pNumber)
                put("otp",otp)
            }
            binding.progressBar.visibility =View.VISIBLE
            binding.buttonVerify.visibility =View.GONE
            val request = JsonObjectRequest(
                Request.Method.POST, url, jsonBody,
                { response ->
                    val status = response.getString("status")
                    val user = response.getJSONObject("user")

                    if (status == "ok") {
                        val id = user.getString("_id")
                        val phoneNumber = user.getString("phoneNumber")
                        val isRegistered = user.getBoolean("isRegisterd")
                        if(isRegistered){
                            val name = user.getString("name")
                            val gender =user.getString("gender")
                            val age = user.getInt("age")
                            editor.putString("NAME",name)
                            editor.putString("GENDER",gender)
                            editor.putInt("AGE",age)
                            editor.apply()
                            startActivity(Intent(this,MainActivity::class.java))
                            finish()
                        }else{
                            var intent =Intent(this,RegisterActivity::class.java)
                            intent.putExtra("ID",id)
                            startActivity(intent)
                            finish()
                        }

                        Log.d("API_RESPONSE", "Status: $status")
                        Log.d("API_RESPONSE", "ID: $id")
                        Log.d("API_RESPONSE", "Phone Number: $phoneNumber")
                        Log.d("API_RESPONSE", "Is Registered: $isRegistered")
                    } else {
                        binding.progressBar.visibility =View.GONE
                        binding.buttonVerify.visibility =View.VISIBLE
                        Toast.makeText(this,"Wrong Otp",Toast.LENGTH_LONG).show()
                    }
                },
                { error ->
                    binding.progressBar.visibility =View.GONE
                    binding.buttonVerify.visibility =View.VISIBLE
                    Toast.makeText(this,"something went wrong",Toast.LENGTH_LONG).show()
                    Log.e("API_ERROR", "Error: ${error.message}")
                }
            )


            requestQueue.add(request)


        }

    }
}