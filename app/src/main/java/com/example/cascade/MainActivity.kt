package com.example.cascade

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.button)
        val pokemonNameEditText = findViewById<EditText>(R.id.pokemonNameEditText)
        val fundsEditText = findViewById<EditText>(R.id.pokemonNameEditText)

        button.setOnClickListener {
            val pokemonName = pokemonNameEditText.text.toString().lowercase()
            val funds = fundsEditText.text.toString()

            if (TextUtils.isDigitsOnly(funds)) {
                val funds = funds.toDouble()

                CoroutineScope(Dispatchers.Main).launch {
                    val cost = calculatePokemonCost(pokemonName, funds)
                    if (cost >= 0) {
                        Toast.makeText(
                            this@MainActivity,
                            "You can afford $pokemonName for $cost",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Toast.makeText(
                            this@MainActivity,
                            "You cannot afford $pokemonName",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            } else {
                Toast.makeText(
                    this@MainActivity,
                    "Please enter a valid numerical value for funds",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

    }

    suspend fun calculatePokemonCost(pokemonName: String, funds: Double): Double {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val pokeApi = retrofit.create(Service::class.java)
        val pokemon = pokeApi.getPokemon(pokemonName)

        val cost =  pokemon.base_experience.toDouble() * 6 * 0.01
        return if (funds >= cost) cost else -1.0
    }


}