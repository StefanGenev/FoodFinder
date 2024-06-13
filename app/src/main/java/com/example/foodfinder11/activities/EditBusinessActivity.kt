package com.example.foodfinder11.activities

import android.app.Activity
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Build
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.example.foodfinder11.R
import com.example.foodfinder11.databinding.ActivityEditBusinessBinding
import com.example.foodfinder11.dto.ResponseWrapper
import com.example.foodfinder11.dto.SaveRestaurantRequestDto
import com.example.foodfinder11.fragments.BusinessProfileFragment
import com.example.foodfinder11.model.FoodType
import com.example.foodfinder11.model.PriceRanges
import com.example.foodfinder11.model.Restaurant
import com.example.foodfinder11.retrofit.RetrofitInstance
import com.example.foodfinder11.utils.AddressUtils
import com.example.foodfinder11.utils.CloudinaryManager
import com.example.foodfinder11.utils.getParcelableExtraProvider
import com.example.foodfinder11.utils.toInt
import com.google.android.gms.maps.model.LatLng
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale

class EditBusinessActivity : BaseNavigatableActivity() {

    companion object {
        const val LATITUDE = "latitude"
        const val LONGITUDE = "longitude"
    }

    private lateinit var binding: ActivityEditBusinessBinding
    private lateinit var restaurant: Restaurant

    private var foodTypes: ArrayList<FoodType> = ArrayList()
    private var selectedFoodType: FoodType = FoodType()
    private var selectedPriceRange: PriceRanges = PriceRanges.CHEAP

    private lateinit var imageUri: Uri
    private var imageChanged: Boolean = false

    private lateinit var address: Address
    private var latLng: LatLng = LatLng(0.0, 0.0)

    private val startFoodTypesActivityForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result: ActivityResult ->

        if (result.resultCode == Activity.RESULT_OK) {

            val intent = result.data
            selectedFoodType = intent?.getParcelableExtraProvider<FoodType>(FoodTypesActivity.FOOD_TYPE)
                ?: FoodType()

            binding.foodTypeTextEdit.setText(selectedFoodType.name)
        }
    }

    private val uploadActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

        if (result.resultCode == Activity.RESULT_OK) {

            val imageUri = result.data?.data

            if (imageUri != null) {

                this.imageUri = imageUri
                imageChanged = true

                viewChosenImage()

            } else {

                showToast("Failed to retrieve image URI")
            }
        }
    }

    private val mapsActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

        if (result.resultCode == Activity.RESULT_OK) {

            val intent = result.data
            latLng = intent?.getParcelableExtraProvider<LatLng>(MapsActivity.PICKED_POINT)!!

            geocodeLatitudeAndLongitude(latLng)
        }
    }

    override fun initializeActivity() {
        binding = ActivityEditBusinessBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun initializeData() {
        restaurant =
            intent.getParcelableExtraProvider<Restaurant>(BusinessProfileFragment.RESTAURANT)!!

        selectedFoodType = restaurant.foodType
        selectedPriceRange = restaurant.priceRange

        latLng = AddressUtils.getLatitudeLongitudeFromString(restaurant.address)
    }

    override fun initializeViews() {

        binding.nameTextEdit.setText(restaurant.name)
        binding.foodTypeTextEdit.setText(restaurant.foodType.name)

        checkSelectedPriceChip()

        binding.chipCheap.setOnClickListener {
            selectedPriceRange = PriceRanges.CHEAP
        }

        binding.chipMedium.setOnClickListener {
            selectedPriceRange = PriceRanges.MIDRANGE
        }

        binding.chipExpensive.setOnClickListener {
            selectedPriceRange = PriceRanges.EXPENSIVE
        }

        Glide.with(this@EditBusinessActivity)
            .load(restaurant.imageUrl)
            .into(binding.menuImage)

        binding.choosePhotoLayout.setOnClickListener {
            choosePhoto()
        }

        binding.chooseAddressLayout.setOnClickListener {
            onClickSelectLocation()
        }

        val latLng = AddressUtils.getLatitudeLongitudeFromString(restaurant.address)
        geocodeLatitudeAndLongitude(latLng)
    }

    private fun checkSelectedPriceChip() {

        binding.chipCheap.isChecked = restaurant.priceRange == PriceRanges.CHEAP
        binding.chipMedium.isChecked = restaurant.priceRange == PriceRanges.MIDRANGE
        binding.chipExpensive.isChecked = restaurant.priceRange == PriceRanges.EXPENSIVE
    }

    override fun loadData(): Boolean {
        loadFoodTypesRequest()
        return true
    }

    override fun commitData(): Boolean {

        if( imageChanged ) {

            uploadImage(imageUri)

        } else {
            saveRestaurantRequest()
        }

        return true
    }

    private fun choosePhoto() {

        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        uploadActivityResultLauncher.launch(intent)
    }

    private fun viewChosenImage() {
        Glide.with(this@EditBusinessActivity)
            .load(imageUri)
            .into(binding.menuImage)
    }

    private fun uploadImage(imageUri: Uri) {

        CloudinaryManager.uploadImage(imageUri, object : CloudinaryManager.OnUploadListener {

            override fun onUploadSuccess(imageUrl: String) {
                restaurant.imageUrl = imageUrl
                saveRestaurantRequest()
            }

            override fun onUploadError(error: String) {
                // Handle upload error
                showToast("Upload error: $error")
            }
        })
    }

    private fun saveRestaurantRequest() {

        val dto = SaveRestaurantRequestDto(id = restaurant.id,
                                            name =  binding.nameTextEdit.text.toString(),
                                            priceRange = selectedPriceRange,
                                            address = AddressUtils.getStringFromLatLng(latLng),
                                            imageUrl = restaurant.imageUrl,
                                            foodTypeCode = selectedFoodType.id
            )

        RetrofitInstance.getApiService().saveRestaurant(dto)

            .enqueue(object : Callback<ResponseWrapper<Restaurant>> {

                override fun onResponse(
                    call: Call<ResponseWrapper<Restaurant>>,
                    response: Response<ResponseWrapper<Restaurant>>
                ) {

                    val responseBody = response.body().takeIf { it != null } ?: return

                    if (responseBody.status == 200) {
                        returnOkIntent()
                    }
                }

                override fun onFailure(
                    call: Call<ResponseWrapper<Restaurant>>,
                    t: Throwable
                ) {
                }
            })
    }

    fun loadFoodTypesRequest() {

        RetrofitInstance.getApiService().getAllFoodTypes()
            .enqueue(object : Callback<ResponseWrapper<List<FoodType>>> {

                override fun onResponse(
                    call: Call<ResponseWrapper<List<FoodType>>>,
                    response: Response<ResponseWrapper<List<FoodType>>>
                ) {

                    val responseBody = response.body().takeIf { it != null } ?: return

                    if (responseBody.status == 200) {

                        val responseData = responseBody.data.takeIf { it != null } ?: return
                        foodTypes.addAll(responseData)

                    } else {
                        Toast.makeText(this@EditBusinessActivity, responseBody.message, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(
                    call: Call<ResponseWrapper<List<FoodType>>>,
                    t: Throwable
                ) {
                    Toast.makeText(this@EditBusinessActivity, getString(R.string.problem_with_request), Toast.LENGTH_SHORT).show()
                }
            })
    }

    fun onClickFoodType(view: View) {

        val intent = Intent(this@EditBusinessActivity, FoodTypesActivity::class.java)
        intent.putExtra(NewBusinessDataActivity.FOOD_TYPES, foodTypes)
        startFoodTypesActivityForResult.launch(intent)
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
        binding.tvCurrentAddress.text = AddressUtils.getAddressVisualisationText(address)
    }

    private fun onClickSelectLocation() {
        val pickPointIntent = Intent(this, MapsActivity::class.java)
        pickPointIntent.putExtra(LATITUDE, latLng.latitude)
        pickPointIntent.putExtra(LONGITUDE, latLng.longitude)
        mapsActivityResultLauncher.launch(pickPointIntent)
    }

}