package com.example.demovideo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.demovideo.DatabaseHelper
import com.example.demovideo.VideoAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper // Declare the database helper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = DatabaseHelper(this) // Initialize the database helper

        // Insert some sample data into the database (optional)
        insertSampleData()

        // Retrieve videos from the database
        val videos = dbHelper.getAllVideos()

        // Find RecyclerView from layout
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)

        // Set up RecyclerView with Adapter to display videos
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = VideoAdapter(this, videos) { selectedVideo ->
            // Handle any additional click actions if needed
        }
    }

    // Insert sample videos into the database
    private fun insertSampleData() {
        val sampleVideos = listOf(
            Video("Get Moving, Feel Amazing", "android.resource://$packageName/${R.raw.sample_video1}", R.drawable.cover),
            Video("Find Calm in Every Breath", "android.resource://$packageName/${R.raw.sample_video2}", R.drawable.cover),
            Video("The Art of Yoga", "android.resource://$packageName/${R.raw.sample_video3}", R.drawable.cover),
            Video("The Present is the Gift", "android.resource://$packageName/${R.raw.sample_video4}", R.drawable.cover),
            Video("10 Simple Tips for Better Sleep: Improve Your Rest Tonight!", "android.resource://$packageName/${R.raw.sample_video5}", R.drawable.cover)
        )

        // Insert each video into the database
        for (video in sampleVideos) {
            dbHelper.insertVideo(video)
        }
    }
}
