package com.example.diceroller3

import android.content.Context
import android.content.SharedPreferences
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import java.text.DateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.temporal.ChronoUnit
import java.util.Locale
import java.util.Timer
import java.util.TimerTask


class MainActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        val greeting = findViewById<TextView>(R.id.textView3)
        val dateEditText = findViewById<EditText>(R.id.editTextDate)
        val date = findViewById<TextView>(R.id.date)
        val calculateButton = findViewById<Button>(R.id.calculateButton)
        val fruitImage = findViewById<ImageView>(R.id.fruitImage)

        val savedInput = getUserInput()
        dateEditText.setText(savedInput)

        calculateButton.setOnClickListener {
            Toast.makeText(this, "hello", Toast.LENGTH_SHORT).show()
            saveUserInput(dateEditText.text.toString())

            val userDate = try {
                LocalDate.parse(dateEditText.text.toString(), DateTimeFormatter.ISO_DATE)
            } catch (e: DateTimeParseException) {
                null
            }

            if (userDate != null) {
                val between = ChronoUnit.WEEKS.between(userDate, LocalDate.now())
                val dayOfWeek = userDate.dayOfWeek.value - LocalDate.now().dayOfWeek.value
                if (between in 4..40) {
                    val fruit = findFruit(between)!!
                    if (dayOfWeek > 0) {
                        date.text = "${dayOfWeek} days to next fruit"
                    } else {
                        date.text = "${dayOfWeek + 7} days to next fruit"
                    }
                    greeting.text = "Hello my Little " + fruit.name + "!!"
                    fruitImage.setImageResource(fruit.picture)
                } else {
                    greeting.text = "Try again!"
                }
            } else {
                greeting.text = "Try again!"
            }
        }

        val stringDate =
            DateFormat.getDateInstance(DateFormat.FULL, Locale.ENGLISH)
                .format(Calendar.getInstance().time)
        val daysToNext = findViewById<TextView>(R.id.daysToNext)
        val time = findViewById<TextView>(R.id.time)

        daysToNext.text = stringDate

        val handler = Handler(Looper.getMainLooper())
        val timer = Timer()
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                handler.post {
                    time.text = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))
                }
            }
        }, 5000, 1000)
    }

    private fun saveUserInput(userInput: String) {
        val editor = sharedPreferences.edit()
        editor.putString("userInput", userInput)
        editor.apply()
    }

    private fun getUserInput(): String {
        return sharedPreferences.getString("userInput", "") ?: ""
    }
}

private fun findFruit(week: Long): Fruit? {
    val fruitList = mutableListOf(
        Fruit("Poppy Seed", 4, R.drawable.poppy_seed),
        Fruit("Apple Seed", 5, R.drawable.apple_seed),
        Fruit("Sweet Pea", 6, R.drawable.sweet_pea),
        Fruit("Blueberry", 7, R.drawable.blueberry),
        Fruit("Raspberry", 8, R.drawable.raspberry),
        Fruit("Green Olive", 9, R.drawable.green_olive),
        Fruit("Kumquat", 10, R.drawable.kumquat),
        Fruit("Lime", 11, R.drawable.lime),
        Fruit("Plum", 12, R.drawable.plum),
        Fruit("Lemon", 13, R.drawable.lemon),
        Fruit("Nectarine", 14, R.drawable.nectarine),
        Fruit("Apple", 15, R.drawable.apple),
        Fruit("Avocado", 16, R.drawable.avocado),
        Fruit("Pear", 17, R.drawable.pear),
        Fruit("Bell Pepper", 18, R.drawable.bell_pepper),
        Fruit("Tomato", 19, R.drawable.tomato),
        Fruit("Artichoke", 20, R.drawable.artichoke),
        Fruit("Carrot", 21, R.drawable.carrot),
        Fruit("Papaya", 22, R.drawable.papaya),
        Fruit("Grapefruit", 23, R.drawable.grapefruit),
        Fruit("Corn", 24, R.drawable.corn),
        Fruit("Rutabaga", 25, R.drawable.rutabaga),
        Fruit("Lettuce", 26, R.drawable.lettuce),
        Fruit("Cauliflower", 27, R.drawable.cauliflower),
        Fruit("Eggplant", 28, R.drawable.eggplant),
        Fruit("Acorn Squash", 29, R.drawable.acorn_squash),
        Fruit("Cabbage", 30, R.drawable.cabbage),
        Fruit("Cucumber", 31, R.drawable.cucumber),
        Fruit("Coconut", 32, R.drawable.coconut),
        Fruit("Jicama", 33, R.drawable.jicama),
        Fruit("Pineapple", 34, R.drawable.pineapple),
        Fruit("Butternut Squash", 35, R.drawable.butternut_squash),
        Fruit("Honeydew", 36, R.drawable.honeydew),
        Fruit("Swiss Chard", 37, R.drawable.swiss_chard),
        Fruit("Winter Melon", 38, R.drawable.winter_melon),
        Fruit("Water Melon", 39, R.drawable.water_melon),
        Fruit("Pumpkin", 40, R.drawable.pumpkin)
    )

    return fruitList.find { it.week == week }
}