package com.marcelm.chucknorrisjokes

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private lateinit var jokeRecyclerView: RecyclerView
    private lateinit var reloadButton: FloatingActionButton

    private val jokes: MutableList<Joke> = mutableListOf()
    private lateinit var jokeAdapter: JokeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        jokeRecyclerView = findViewById(R.id.jokeRecyclerView)
        reloadButton = findViewById(R.id.reloadButton)

        jokeAdapter = JokeAdapter(jokes)
        jokeRecyclerView.layoutManager = LinearLayoutManager(this)
        jokeRecyclerView.adapter = jokeAdapter

        reloadButton.setOnClickListener {
            jokes.clear()
            for (number in 1..10) {
                fetchJokes()
            }
        }

        jokes.clear()
        for (number in 1..10) {
            fetchJokes()
        }
    }

    // Inside the MainActivity class
    private fun fetchJokes() {
        // Perform the network request in a try-catch block
        try {
            val client = OkHttpClient()

            val request = Request.Builder()
                .url("https://api.chucknorris.io/jokes/random")
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                    runOnUiThread {
                        Toast.makeText(this@MainActivity, "Failed to fetch jokes", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.code == 200) {
                        val responseData = response.body?.string()
                        val jokeObject = JSONObject(responseData)

                        val joke = Joke(jokeObject.getString("id"), jokeObject.getString("value"))

                        runOnUiThread {
                            jokes.add(joke)
                            jokeAdapter.notifyDataSetChanged() // Notify the adapter of the data change
                        }
                    } else {
                        runOnUiThread {
                            Toast.makeText(this@MainActivity, "Failed to fetch jokes, API responded with $response.code", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
            runOnUiThread {
                Toast.makeText(this@MainActivity, "Failed to fetch jokes", Toast.LENGTH_SHORT).show()
            }
        }
    }

}
