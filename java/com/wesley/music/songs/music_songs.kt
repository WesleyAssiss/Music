package com.wesley.music.songs


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wesley.music.databinding.ActivitySongBinding
import com.wesley.music.model.music_model

class music_songs(private var songs: List<music_model>): RecyclerView.Adapter<music_songs.MusicItemHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): music_songs.MusicItemHolder {
        val binding: ActivitySongBinding = ActivitySongBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MusicItemHolder(binding)
    }

    override fun onBindViewHolder(holder: music_songs.MusicItemHolder, position: Int) {
        holder.bind(this.songs[position])
    }

    override fun getItemCount(): Int {
        return this.songs.size
    }

    class MusicItemHolder(private var binding: ActivitySongBinding):
        RecyclerView.ViewHolder(binding.root){
        fun bind(get: music_model){
            binding.trackname.text = get.trackName
            binding.collectionname.text = get.collectionName
            binding.releasedate.text = get.releaseDate
        }
    }
}