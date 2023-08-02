package com.example.bmicalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.bmicalculator.databinding.ActivityMainBinding


class MainActivity() : AppCompatActivity() {
    //Binding with activity_main.xml created to skip using findviewbyid
    lateinit var binding: ActivityMainBinding
    //val binding = ActivityMainBinding.bind(findViewById(R.id.root))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.resultButton.setOnClickListener { chk_age()}


        }
    fun chk_age(){
        val age_var = binding.userAge.editText?.text.toString()
        val age = age_var.toIntOrNull()
        if (age != null) {
            if (age <= 3){
                age_toast()
            }else{
                calc_bmi()
            }
        }
    }



    fun calc_bmi() = try {
        val heightvar = binding.userHeight.editText?.text.toString()
        val height = heightvar.toDoubleOrNull()
        val weightvar = binding.userWeight.editText?.text.toString()
        val weight = weightvar.toDoubleOrNull()
        //if switch in cms
        val switch_ci: Boolean = binding.switch1.isChecked

        if (switch_ci) {
            val heightm = height?.times(0.0254)
            bmi(weight,heightm)
        }else {
            val heightm = height?.div(100)
            bmi(weight,heightm)
        }

        //val bmi: Double? = height_m?.times(height_m)?.let { weight?.div(it) }
        //val bmi: Double? = weight?.div(height_m?.times(height_m?))
    }catch (e: NumberFormatException) {
        toasty("Enter valid Height or Weight")
    }

    fun bmi(weight: Double?, heightm:Double?){

        val bmi: Double? = weight?.div(heightm?.times(heightm)!!)

        when (bmi!!) {

            in Double.MIN_VALUE..3.00 -> {
                toast_fun() }
            in Double.MAX_VALUE..35.00 -> {
                toast_fun()
            }
            //toast_fun defined later
            else -> {
                val formattedbmi = String.format("%.2f", bmi).toDouble()
                binding.bmiResult.text = formattedbmi.toString()
                //val intbmi = formattedbmi.toDouble()

                //seekbar code block
                when (formattedbmi) {
                    in 3.0..5.0 -> binding.seekBar.progress = 1
                    in 5.0..9.0 -> binding.seekBar.progress = 2
                    in 9.0..12.0 -> binding.seekBar.progress = 3
                    in 12.0..18.0 -> binding.seekBar.progress = 4
                    in 18.0..24.0 -> binding.seekBar.progress = 5
                    in 24.0..28.0 -> binding.seekBar.progress = 6
                    in 28.0..30.0 -> binding.seekBar.progress = 7
                    in 30.0..31.0 -> binding.seekBar.progress = 8
                    in 31.0..33.0 -> binding.seekBar.progress = 9
                    in 33.0..40.0 -> binding.seekBar.progress = 10
                    else -> {
                        binding.seekBar.progress = Int.MIN_VALUE
                    }
                }
            }
        }
    }

    fun toasty(message: String){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }


    fun toast_fun() {
        Toast.makeText(this, "The Entered Values of BMI are incorrect", Toast.LENGTH_SHORT).show()
    }

    fun age_toast() {
        Toast.makeText(
            this,
            "BMI cannot be calculated for this Age .Please try for another age",
            Toast.LENGTH_LONG
        ).show()
    }
}
