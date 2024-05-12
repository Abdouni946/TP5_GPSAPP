package com.example.tp5

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.motion.widget.Debug.getLocation
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class Getlocation : AppCompatActivity() {
    private var likeCount = 0 // Initial like count
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val imageView = findViewById<ImageView>(R.id.ImageView)
        val latitudeTextView = findViewById<TextView>(R.id.tvLatitude)
        val longitudeTextView = findViewById<TextView>(R.id.tvLongitude)
        val tvLikeCount = findViewById<TextView>(R.id.tvLikeCount)
        val btnLike = findViewById<Button>(R.id.btnLike)

        // Retrieve image and coordinates from intent
        val byteArray = intent.getByteArrayExtra("image")
        val lat = intent.getStringExtra("latitude")
        val lon = intent.getStringExtra("longitude")

        // Set image in ImageView
        if (byteArray != null) {
            val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
            imageView.setImageBitmap(bitmap)
        }

        // Update latitude and longitude TextViews
        latitudeTextView.text = "Latitude: $lat"
        longitudeTextView.text = "Longitude: $lon"

        // Set up the like button
        btnLike.setOnClickListener {
            likeCount++ // Increment like count
            tvLikeCount.text = "Likes: $likeCount" // Update the TextView to show the new count
        }
    }

}