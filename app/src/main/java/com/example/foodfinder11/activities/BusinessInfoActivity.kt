package com.example.foodfinder11.activities

import android.location.Address
import android.location.Geocoder
import android.os.Build
import com.example.foodfinder11.R
import com.example.foodfinder11.databinding.ActivityBusinessInfoBinding
import com.example.foodfinder11.fragments.BusinessProfileFragment
import com.example.foodfinder11.model.Restaurant
import com.example.foodfinder11.utils.AddressUtils
import com.example.foodfinder11.utils.getParcelableExtraProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.Locale


class BusinessInfoActivity : BaseNavigatableActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityBusinessInfoBinding
    private lateinit var mMap: GoogleMap
    private lateinit var restaurant: Restaurant
    private lateinit var address: Address

    override fun initializeActivity() {
        binding = ActivityBusinessInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun initializeData() {
        restaurant =
            intent.getParcelableExtraProvider<Restaurant>(BusinessProfileFragment.RESTAURANT)!!
    }

    override fun initializeViews() {

        val latLng = AddressUtils.getLatitudeLongitudeFromString(restaurant.address)
        geocodeLatitudeAndLongitude(latLng)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment

        mapFragment.getMapAsync(this)

        binding.tvPhoneNumber.text = restaurant.phoneNumber
    }

    override fun onMapReady(googleMap: GoogleMap) {

        mMap = googleMap

        val latLng = AddressUtils.getLatitudeLongitudeFromString(restaurant.address)
        val initialMarker = LatLng(latLng.latitude, latLng.longitude)
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(initialMarker, 16f)

        // Add initial marker and move the camera
        val addressText = AddressUtils.getAddressVisualisationText(address)
        mMap.addMarker(MarkerOptions().position(initialMarker).title(addressText))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(initialMarker))
        mMap.animateCamera(cameraUpdate)
    }

    private fun geocodeLatitudeAndLongitude(latLng: LatLng) {

        val geocodeListener = Geocoder.GeocodeListener { addresses ->
            address = addresses[0]!!
            showSelectedLocation(address)
        }

        val geocoder = Geocoder(this, Locale.getDefault())

        if (Build.VERSION.SDK_INT >= 33) {
            // declare here the geocodeListener, as it requires Android API 33
            geocoder.getFromLocation(latLng.latitude
                ,latLng.longitude
                ,1
                , geocodeListener)

        } else {
            // For Android SDK < 33, the addresses list will be still obtained from the getFromLocation() method
            val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude,1)
            address = addresses!![0]
            showSelectedLocation(address)
        }
    }

    private fun showSelectedLocation(address: Address) {

        binding.tvAddressValue.text = AddressUtils.getAddressVisualisationText(address)
    }
}