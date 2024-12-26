package com.example.demovideo

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.VideoView
import androidx.recyclerview.widget.RecyclerView

class VideoAdapter(
    private val context: Context,
    private val videoList: List<Video>,
    private val onVideoClick: (Video) -> Unit
) : RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {

    inner class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.videoTitle)
        val videoView: VideoView = itemView.findViewById(R.id.videoViewItem)
        val videoCover: ImageView = itemView.findViewById(R.id.videoCover)  // Add this line
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_video, parent, false)
        return VideoViewHolder(view)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val video = videoList[position]
        holder.title.text = video.title
        holder.videoCover.setImageResource(video.coverPhoto)

        holder.itemView.setOnClickListener {
            // Hide the cover photo and show the video view
            holder.videoCover.visibility = View.GONE
            holder.videoView.visibility = View.VISIBLE

            // Check if the video is already playing
            if (holder.videoView.isPlaying) {
                // Pause the video if it's currently playing
                holder.videoView.pause()
            } else {
                // Start the video if it's not playing
                holder.videoView.setVideoURI(Uri.parse(video.uri))
                holder.videoView.start()
            }

            // Trigger any additional actions when the video is clicked
            onVideoClick(video)
        }
    }

    override fun getItemCount() = videoList.size
}
