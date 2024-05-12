package com.example.tp5

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.graphics.Bitmap
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.io.ByteArrayOutputStream

class MainActivity : AppCompatActivity() {
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var latitude: TextView
    private lateinit var longitude: TextView
    private lateinit var imageView: ImageView
    private lateinit var button1: Button

    private val cameraPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            takePicture()
        } else {
            Toast.makeText(this, "Camera permission is required to use the camera.", Toast.LENGTH_SHORT).show()
        }
    }

    private val takePictureLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val imageBitmap = result.data?.extras?.get("data") as Bitmap
            imageView.setImageBitmap(imageBitmap)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_getlocation)

        imageView = findViewById(R.id.ImageView)
        button1 = findViewById(R.id.button1)
        button1.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                cameraPermissionRequest.launch(android.Manifest.permission.CAMERA)
            } else {
                takePicture()
            }
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        latitude = findViewById(R.id.latitude)
        longitude = findViewById(R.id.longitude)
        val getLocationButton = findViewById<Button>(R.id.button)
        getLocationButton.setOnClickListener {
            getLocation()
        }

        val btnSave = findViewById<Button>(R.id.btnSave)
        btnSave.setOnClickListener {
            imageView.isDrawingCacheEnabled = true
            val bitmap = Bitmap.createBitmap(imageView.drawingCache)
            imageView.isDrawingCacheEnabled = false

            // Convert bitmap to byte array to pass with Intent
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val byteArray = stream.toByteArray()

            val intent = Intent(this, Getlocation::class.java).apply {
                putExtra("image", byteArray)
                putExtra("latitude", latitude.text.toString())
                putExtra("longitude", longitude.text.toString())
            }


            startActivity(intent)
        }
    }



    private fun takePicture() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            takePictureLauncher.launch(takePictureIntent)
        } catch (e: Exception) {
            Toast.makeText(this, "Failed to open the camera.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 100)
        } else {
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    latitude.text = location.latitude.toString()
                    longitude.text = location.longitude.toString()
                }
            }
        }
    }


}
