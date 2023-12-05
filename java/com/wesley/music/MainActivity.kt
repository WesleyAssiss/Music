// MainActivity.kt
package com.wesley.music

import android.os.Bundle
import android.os.StrictMode
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonParser
import com.wesley.music.databinding.ActivityMainBinding
import com.wesley.music.model.music_model
import com.wesley.music.songs.music_songs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.URL

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var songsAdapter: music_songs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()

        binding.search.editText?.addTextChangedListener {
            search(it.toString())
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        songsAdapter = music_songs(emptyList())
        binding.recyclerView.adapter = songsAdapter
    }

    private fun search(content: String) {
        backgroundDownload(content)
    }

    private fun backgroundDownload(content: String) {
        GlobalScope.launch(Dispatchers.IO) {
            val policyThread = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policyThread)

            val url = URL("https://itunes.apple.com/search?term=$content")
            println("URL $url")

            try {
                val req = url.openConnection()
                val buffer = req.getInputStream().bufferedReader()

                val response = buffer.readText()

                val musics = parserLines(response)

                launch(Dispatchers.Main) {
                    songsAdapter = music_songs(musics)
                    binding.recyclerView.adapter = songsAdapter
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun parserLines(response: String): List<music_model> {
        val jsonObject = JsonParser.parseString(response).asJsonObject

        val resultsArray = jsonObject.getAsJsonArray("results")

        val musicList = mutableListOf<music_model>()

        for (jsonElement in resultsArray) {
            try {
                val jsonObject = jsonElement.asJsonObject
                val music_model = music_model(
                    jsonObject.getAsJsonPrimitive("trackName").asString,
                    jsonObject.getAsJsonPrimitive("collectionName").asString,
                    jsonObject.getAsJsonPrimitive("releaseDate").asString.toString().split("T")[0]
                )

                musicList.add(music_model)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return musicList
    }
}
