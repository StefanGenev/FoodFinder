package com.example.foodfinder11.activities

import android.app.Activity
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.foodfinder11.databinding.ActivityChooseAddressBinding
import com.example.foodfinder11.dto.NoData
import com.example.foodfinder11.dto.RegisterRestaurantRequestDto
import com.example.foodfinder11.dto.RegisterRestaurantResponseDto
import com.example.foodfinder11.dto.ResponseWrapper
import com.example.foodfinder11.dto.SaveRestaurantLocationRequestDto
import com.example.foodfinder11.retrofit.RetrofitInstance
import com.example.foodfinder11.utils.AddressUtils
import com.example.foodfinder11.utils.SessionManager
import com.example.foodfinder11.utils.getParcelableExtraProvider
import com.google.android.gms.maps.model.LatLng
import com.intuit.sdp.BuildConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale


class ChooseAddressActivity : BaseNavigatableActivity() {

    private lateinit var binding: ActivityChooseAddressBinding

    private lateinit var address: Address
    private lateinit var latLng: LatLng

    private val mapsActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

        if (result.resultCode == Activity.RESULT_OK) {

            val intent = result.data
            latLng = intent?.getParcelableExtraProvider<LatLng>(MapsActivity.PICKED_POINT)!!

            geocodeLatitudeAndLongitude(latLng)
        }
    }

    override fun initializeActivity() {
        binding = ActivityChooseAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun initializeViews() {

        binding.selectLocationLayout.setOnClickListener {
            onClickSelectLocation()
        }
    }

    override fun commitData(): Boolean {

        saveRestaurantLocationRequest()

        return true
    }

    private fun onClickSelectLocation() {
        val pickPointIntent = Intent(this, MapsActivity::class.java)
        mapsActivityResultLauncher.launch(pickPointIntent)
    }

    private fun geocodeLatitudeAndLongitude(latLng: LatLng) {

        val geocodeListener = Geocoder.GeocodeListener { addresses ->
            address = addresses[0]
            showSelectedLocation(address!!)
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
            showSelectedLocation(address!!)
        }
    }

    private fun showSelectedLocation(address: Address) {
        binding.selectLocationText.text = AddressUtils.getAddressVisualisationText(address)
    }

    private fun saveRestaurantLocationRequest() {

        val restaurantId = SessionManager.fetchRestaurant().id

        val dto = SaveRestaurantLocationRequestDto(restaurantId, AddressUtils.getStringFromLatLng(latLng))

        RetrofitInstance.getApiService().saveRestaurantLocation(dto)
            .enqueue(object : Callback<ResponseWrapper<NoData>> {

                override fun onResponse(
                    call: Call<ResponseWrapper<NoData>>,
                    response: Response<ResponseWrapper<NoData>>
                ) {

                    val responseBody = response.body().takeIf { it != null } ?: return

                    if (responseBody.status == 200) {

                        val responseData = responseBody.data.takeIf { it != null } ?: return
                        openNextActivity()

                    } else {
                        Toast.makeText(this@ChooseAddressActivity, responseBody.message, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(
                    call: Call<ResponseWrapper<NoData>>,
                    t: Throwable
                ) {
                    Toast.makeText(this@ChooseAddressActivity, "Problem with request", Toast.LENGTH_SHORT).show()
                }
            })

    }

    private fun openNextActivity() {
        val intent = Intent(this@ChooseAddressActivity, RestaurantRegisteredActivity::class.java)
        startActivity(intent)
    }
}