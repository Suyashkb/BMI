package com.example.bmicalculator

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.RadioButton
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.bmicalculator.databinding.ActivityRegisterBinding
import org.json.JSONObject

class RegisterActivity : AppCompatActivity() {
    lateinit var binding : ActivityRegisterBinding
    lateinit var id : String
    private lateinit var requestQueue: RequestQueue
    private val url = "http://3.6.88.40:3001/api/v1/auth/"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreference =  getSharedPreferences("BMI_DATA", Context.MODE_PRIVATE)
        var editor = sharedPreference.edit()


        setupEditTextFocusChangeListeners()
        setupClickOutsideEditTextToHideKeyboard(binding.root)

        id = intent.getStringExtra("ID") as String

        requestQueue = Volley.newRequestQueue(this)

        var genderRadio = binding.genderRadio
        genderRadio.clearCheck()

        binding.registerBtn.setOnClickListener {
            binding.progressBar.visibility =View.VISIBLE
            binding.registerBtn.visibility =View.GONE
            var name:String = binding.nameEdit.text.toString()
            var age:Int = binding.ageEdit.text.toString().toInt()
            var checkedId: Int = genderRadio!!.checkedRadioButtonId
            var gender = findViewById<RadioButton>(checkedId).text.toString()
            val jsonBody = JSONObject().apply {
                put("name", name)
                put("gender",gender)
                put("age",age)
            }
            if(name!="" || age!=0 || gender!=""){
                val request = JsonObjectRequest(
                    Request.Method.PUT, url+id, jsonBody,
                    { response ->
                        val status = response.getString("status")
                        val user = response.getJSONObject("user")
                        if (status=="ok") {
                            var userName = user.getString("name")
                            var userage = user.getInt("age")
                            var userGender = user.getString("gender")
                            editor.putString("NAME",userName)
                            editor.putInt("AGE",age)
                            editor.putString("GENDER",gender)
                            editor.apply()
                            var intent = Intent(this,MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }else {
                            binding.registerBtn.visibility = View.VISIBLE
                            binding.progressBar.visibility=View.GONE
                            Toast.makeText(this,"Server error", Toast.LENGTH_SHORT).show()
                        }


                    },
                    { error ->
                        binding.ageEdit.setText("")
                        binding.nameEdit.setText("")
                        binding.genderRadio.clearCheck()
                        binding.registerBtn.visibility = View.VISIBLE
                        binding.progressBar.visibility=View.GONE
                        Toast.makeText(this,"something went wrong", Toast.LENGTH_SHORT).show()
                        Log.e("API_ERROR", "Error: ${error.message}")
                    }
                )

                requestQueue.add(request)

            }
        }

    }
    private fun setupEditTextFocusChangeListeners() {
        binding.nameEdit.setOnEditorActionListener { _, _, _ ->
            binding.ageEdit.requestFocus()
            true
        }

        binding.ageEdit.setOnEditorActionListener { _, _, _ ->
            binding.genderRadio.requestFocus()
            true
        }
    }

    private fun setupClickOutsideEditTextToHideKeyboard(view: View) {
        if (view is ConstraintLayout) {
            view.setOnTouchListener { _, event ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view.windowToken, 0)
                }
                false
            }
        }
    }
}