package com.example.foodfinder11.activities


import android.app.Activity
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.example.foodfinder11.databinding.ActivityConfirmOrderBinding
import com.example.foodfinder11.dto.NoData
import com.example.foodfinder11.dto.RegisterResponseDto
import com.example.foodfinder11.dto.ResponseWrapper
import com.example.foodfinder11.model.FoodType
import com.example.foodfinder11.model.PaymentMethods
import com.example.foodfinder11.model.PriceRanges
import com.example.foodfinder11.model.Roles
import com.example.foodfinder11.retrofit.RetrofitInstance
import com.example.foodfinder11.utils.AddressUtils
import com.example.foodfinder11.utils.Constants
import com.example.foodfinder11.utils.SessionManager
import com.example.foodfinder11.utils.getParcelableExtraProvider
import com.example.foodfinder11.utils.toEnum
import com.google.android.gms.maps.model.LatLng
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale

class ConfirmOrderActivity : BaseNavigatableActivity() {

    private lateinit var binding: ActivityConfirmOrderBinding

    private lateinit var address: Address
    private var latLng: LatLng = LatLng(0.0, 0.0)

    private val mapsActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

        if (result.resultCode == Activity.RESULT_OK) {

            val intent = result.data
            latLng = intent?.getParcelableExtraProvider<LatLng>(MapsActivity.PICKED_POINT)!!

            geocodeLatitudeAndLongitude(latLng)
        }
    }

    private val startPaymentMethodActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result: ActivityResult ->

        if (result.resultCode == Activity.RESULT_OK) {

            updatePaymentMethod()
        }
    }

    override fun initializeActivity() {
        binding = ActivityConfirmOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun initializeViews() {

        updateTotal()
        updatePaymentMethod()
        binding.address.text = "Choose address"

        binding.chooseAddressLayout.setOnClickListener {
            onClickSelectLocation()
        }

        binding.paymentMethodLayout.setOnClickListener {
            onClickChoosePaymentMethod()
        }
    }

    override fun commitData(): Boolean {

        confirmOrderRequest()

        return true
    }

    private fun confirmOrderRequest() {

        val order = SessionManager.fetchOrder()

        RetrofitInstance.getApiService().confirmOrder(order)
            .enqueue(object : Callback<ResponseWrapper<NoData>> {

                override fun onResponse(
                    call: Call<ResponseWrapper<NoData>>,
                    response: Response<ResponseWrapper<NoData>>
                ) {

                    val responseBody = response.body().takeIf { it != null } ?: return

                    if (responseBody.status == 200) {

                        SessionManager.resetOrder()
                        startNextActivity()

                    } else {
                        Toast.makeText(this@ConfirmOrderActivity, responseBody.message, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(
                    call: Call<ResponseWrapper<NoData>>,
                    t: Throwable
                ) {
                    Toast.makeText(this@ConfirmOrderActivity, "Problem with request", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun startNextActivity() {
        val intent = Intent(this@ConfirmOrderActivity, SuccessfulOrderActivity::class.java)
        startActivity(intent)
    }

    private fun updatePaymentMethod() {

        val order = SessionManager.fetchOrder()
        binding.paymentMethod.text = order.getPaymentMethodName()
    }

    private fun updateTotal() {

        val order = SessionManager.fetchOrder()

        if (order.orderItems.isEmpty())
            finish()

        binding.subtotalPrice.text = "${String.format("%.2f", order.getOrderPrice())} lv."
        binding.deliveryPrice.text = "${String.format("%.2f", Constants.DEFAULT_DELIVERY_PRICE)} lv."
        binding.discountPrice.text = "0.0 lv." //TODO: Add discount functionality

        val totalPrice = order.getOrderPrice() + Constants.DEFAULT_DELIVERY_PRICE
        binding.totalPrice.text = "${String.format("%.2f", totalPrice)} lv."
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
        binding.address.text = AddressUtils.getAddressVisualisationText(address)
        binding.address.invalidate()
    }

    private fun onClickSelectLocation() {

        val pickPointIntent = Intent(this, MapsActivity::class.java)
        pickPointIntent.putExtra(EditBusinessActivity.LATITUDE, latLng.latitude)
        pickPointIntent.putExtra(EditBusinessActivity.LONGITUDE, latLng.longitude)
        mapsActivityResultLauncher.launch(pickPointIntent)
    }

    private fun onClickChoosePaymentMethod() {

        val pickPointIntent = Intent(this, ChoosePaymentMethodActivity::class.java)
        startPaymentMethodActivityResultLauncher.launch(pickPointIntent)
    }

}