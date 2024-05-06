package com.example.foodfinder11.activities

import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.example.foodfinder11.R
import com.example.foodfinder11.databinding.ActivityMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException


class MapsActivity : BaseNavigatableActivity(), OnMapReadyCallback {

    companion object {
        const val PICKED_POINT = "picked_point"

        const val INITIAL_LATITUDE = 43.2
        const val INITIAL_LONGITUDE = 27.9
    }

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    override fun initializeActivity() {
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun initializeViews() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment

        // adding on query listener for our search view.

        binding.idSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {

                // on below line we are getting the location name from search view.
                val location: String = binding.idSearchView.query.toString()

                // below line is to create a list of address
                // where we will store the list of all address.
                var addressList: List<Address>? = null

                // checking if the entered location is null or not.
                if (location != null || location == "") {
                    // on below line we are creating and initializing a geo coder.
                    val geocoder = Geocoder(this@MapsActivity)
                    try {
                        // on below line we are getting location from the
                        // location name and adding that location to address list.
                        addressList = geocoder.getFromLocationName(location, 1)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                    // on below line we are getting the location
                    // from our list a first position.
                    val address: Address = addressList!![0]

                    // on below line we are creating a variable for our location
                    // where we will add our locations latitude and longitude.
                    val latLng = LatLng(address.latitude, address.longitude)

                    // on below line we are adding marker to that position.
                    mMap.addMarker(MarkerOptions().position(latLng).title(location))

                    // below line is to animate camera to that position.
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10f))
                }

                return false
            }

        })

        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add initial marker and move the camera
        val initialMarker = LatLng(INITIAL_LATITUDE, INITIAL_LONGITUDE)
        mMap.addMarker(MarkerOptions().position(initialMarker))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(initialMarker))

        mMap.setOnMapClickListener { latLng ->
            val returnIntent = Intent()
            returnIntent.putExtra("picked_point", latLng)
            setResult(RESULT_OK, returnIntent)
            finish()
        }
    }
}