package com.example.bmicalculator

import android.R.drawable
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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

    fun bmi(weight: Double?, heightm:Double?) {

        val bmi: Double? = weight?.div(heightm?.times(heightm)!!)

        // Show the diet tips scroll view
        binding.dietTipsScrollView.visibility = View.VISIBLE

        when (bmi!!) {

            in Double.MIN_VALUE..3.00 -> {
                toast_fun()
            }

            in Double.MAX_VALUE..35.00 -> {
                toast_fun()
            }
            //toast_fun defined later
            else -> {
                val formattedbmi = String.format("%.2f", bmi).toDouble()

                val bmiCategory: Int

                if (formattedbmi > 0.0 && formattedbmi < 12.0) {
                    bmiCategory = 1
                } else if (formattedbmi >= 12.0 && formattedbmi < 18.0) {
                    bmiCategory = 1
                } else if (formattedbmi >= 18.0 && formattedbmi < 24.0) {
                    bmiCategory = 2
                } else if (formattedbmi >= 24.0 && formattedbmi < 35.0) {
                    bmiCategory = 3
                } else {
                    bmiCategory = 3
                }



                binding.bmiResult.text = formattedbmi.toString()
                //val intbmi = formattedbmi.toDouble()
                when(bmiCategory){
                    1->{
                        //add underweight diet tip images
                        //dice_1 and dice_2 are  tips images
                        val image1=findViewById<ImageView>(R.id.img1)
                        val diet_image1=R.drawable.dice_1
                        image1.setImageResource(diet_image1)
                        val image2=findViewById<ImageView>(R.id.img2)
                        val diet_image2=R.drawable.dice_2
                        image2.setImageResource(diet_image2)
                    }
                    2->{
                        //add normal weight diet tips
                        //dice_2 and dice_3 are tips images
                        val image1=findViewById<ImageView>(R.id.img1)
                        val diet_image1=R.drawable.dice_2
                        image1.setImageResource(diet_image1)
                        val image2=findViewById<ImageView>(R.id.img2)
                        val diet_image2=R.drawable.dice_3
                        image2.setImageResource(diet_image2)
                    }
                    3->{
                        //add overweight diet tips
                        //dice_3 and dice_4 are tips images
                        val image1=findViewById<ImageView>(R.id.img1)
                        val diet_image1=R.drawable.dice_3
                        image1.setImageResource(diet_image1)
                        val image2=findViewById<ImageView>(R.id.img2)
                        val diet_image2=R.drawable.dice_4
                        image2.setImageResource(diet_image2)
                    }
                }


                val dietImagesUnderweight = listOf(
                    R.drawable.diet_chart,
                    R.drawable.dice_1,
                    R.drawable.dice_2
                )
                val dietImagesNormal = listOf(
                    R.drawable.diet_chart,
                    R.drawable.dice_2,
                    R.drawable.dice_3
                )
                val dietImagesOverweight = listOf(
                    R.drawable.diet_chart,
                    R.drawable.dice_3,
                    R.drawable.dice_4
                )

                fun updateDietTips(bmiCategory: Int) {
                    val dietImages: List<Int> = when (bmiCategory) {
                        1 -> dietImagesUnderweight
                        2 -> dietImagesNormal
                        3 -> dietImagesOverweight
                        else -> emptyList()
                    }
                    binding.dietTipsLayout.removeAllViews()


                    // Add new ImageViews for diet tips
                    // Add list of suitable images for diet chart
                    dietImages.forEach { imageResource ->
                        val imageView = ImageView(this)
                        imageView.setImageResource(imageResource)
                        binding.dietTipsLayout.addView(dietImages)

                    }


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
private fun LinearLayout.addView(dietImages: List<Int>) {

}
