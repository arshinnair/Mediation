package com.example.demovideo

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

// This class helps you create and manage the database
class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    // This is called when the database is first created
    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = """
            CREATE TABLE $TABLE_VIDEOS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_TITLE TEXT,
                $COLUMN_URI TEXT,
                $COLUMN_COVER_PHOTO INTEGER
            )
        """
        db?.execSQL(createTableQuery)
    }

    // Insert video data into the database, ensuring no duplicates
    fun insertVideo(video: Video) {
        val db = writableDatabase

        // Check if the video already exists using URI (unique for each video)
        val cursor = db.rawQuery("SELECT * FROM $TABLE_VIDEOS WHERE $COLUMN_URI = ?", arrayOf(video.uri))

        if (cursor.count == 0) {  // Only insert if not already exists
            val values = ContentValues().apply {
                put(COLUMN_TITLE, video.title)
                put(COLUMN_URI, video.uri)
                put(COLUMN_COVER_PHOTO, video.coverPhoto)
            }
            db.insert(TABLE_VIDEOS, null, values)
            Log.d("DatabaseHelper", "Video inserted: ${video.title}")
        } else {
            Log.d("DatabaseHelper", "Video already exists: ${video.title}")
        }

        cursor.close()  // Ensure cursor is closed after use
        db.close()  // Close database connection
    }

    // Retrieve all videos from the database
    fun getAllVideos(): List<Video> {
        val videos = mutableListOf<Video>()
        val db = readableDatabase
        val selectQuery = "SELECT * FROM $TABLE_VIDEOS"
        val cursor = db.rawQuery(selectQuery, null)

        // Check if cursor has data
        if (cursor != null && cursor.moveToFirst()) {
            do {
                val titleIndex = cursor.getColumnIndex(COLUMN_TITLE)
                val uriIndex = cursor.getColumnIndex(COLUMN_URI)
                val coverPhotoIndex = cursor.getColumnIndex(COLUMN_COVER_PHOTO)

                if (titleIndex != -1 && uriIndex != -1 && coverPhotoIndex != -1) {
                    val title = cursor.getString(titleIndex)
                    val uri = cursor.getString(uriIndex)
                    val coverPhoto = cursor.getInt(coverPhotoIndex)
                    videos.add(Video(title, uri, coverPhoto))
                }
            } while (cursor.moveToNext())
        }
        cursor.close()  // Close the cursor
        db.close()  // Close database connection

        Log.d("DatabaseHelper", "Videos retrieved: ${videos.size}")
        return videos
    }

    // Handle database version changes (drop and recreate the table)
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropTableQuery = "DROP TABLE IF EXISTS $TABLE_VIDEOS"
        db?.execSQL(dropTableQuery)
        onCreate(db)
    }

    // Constants for database and table names
    companion object {
        private const val DATABASE_NAME = "videos.db"
        private const val DATABASE_VERSION = 1
        const val TABLE_VIDEOS = "videos"
        const val COLUMN_ID = "id"
        const val COLUMN_TITLE = "title"
        const val COLUMN_URI = "uri"
        const val COLUMN_COVER_PHOTO = "cover_photo"
    }
}
