package com.example.geocoding

import android.content.Intent
import android.location.Geocoder
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import java.io.IOException

private const val TAG = "GEOCODE_PLACE_ACTIVITY"

class MainActivity : AppCompatActivity() {
    private lateinit var mapButton: Button
    private lateinit var placeNameInput: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mapButton = findViewById(R.id.map_button)
        placeNameInput = findViewById(R.id.place_name_input)

        mapButton.setOnClickListener {
           getTextInfo()
        }
    }

    private fun getTextInfo() {
        // Grabs the text from the view and converts to a string
        val placeName = placeNameInput.text.toString()
        // Checks to see if the view was empty.
        if(placeName.isBlank()) {
            Toast.makeText(this,
                getString(R.string.no_place_entered_error), Toast.LENGTH_LONG).show()
        } else {
            Log.d(TAG, "About to gecode $placeName")
            showMapForPlace(placeName)
        }
    }

    private fun showMapForPlace(placeName: String) {
        // Geocoder is an object that grab locational data.
        val geocoder = Geocoder(this)
        try {
            // Tries to get location from a name and stores the location in a mutable list.
            val addresses = geocoder.getFromLocationName(placeName, 1)
            // Checks to see if our list is empty.
            if (addresses.isNotEmpty()) {
                // If it is not empty we grab the first item in our list.
                val address = addresses.first()
                // Log to see what the first entry is.
                Log.d(TAG, "First address is $address")
                // We pass the latitude and longitude data from our location to another string.
                val geoUriString = "geo:${address.latitude},${address.longitude}" // "geo:45,-90"
                Log.d(TAG, "Using geo uri $geoUriString")
                // We then convert that into a Uri
                val geoUri = Uri.parse(geoUriString)
                // We than create an intent to try and use this data in another activity.
                val mapIntent = Intent(Intent.ACTION_VIEW, geoUri)
                Log.d(TAG, "Launching map activity")
                startActivity(mapIntent)
            } else {
                // If our list is empty and we did not find a location, we log the place that we
                // couldn't find and we give the user a warning.
                Log.d(TAG, "No places found for string $placeName")
                Toast.makeText(this, getString(R.string.no_place_error), Toast.LENGTH_LONG).show()
            }
        } catch (e: IOException) {
            // If we have no service or internet this will catch an IOException and Toast the user.
            Log.e(TAG, "Unable to Geocode place $placeName", e)
            Toast.makeText(this,
                getString(R.string.connection_error_warning), Toast.LENGTH_LONG).show()
        }

    }
}