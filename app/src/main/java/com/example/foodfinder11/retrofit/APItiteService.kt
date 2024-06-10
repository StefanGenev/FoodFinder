package com.example.foodfinder11.retrofit

import com.example.foodfinder11.dto.AddRemoveFavoriteRestaurantRequestDto
import com.example.foodfinder11.dto.CheckIfEmailExistsRequestDto
import com.example.foodfinder11.dto.CheckIfEmailExistsResponseDto
import com.example.foodfinder11.dto.IdentifierDto
import com.example.foodfinder11.dto.LoginRequestDto
import com.example.foodfinder11.dto.LoginResponseDto
import com.example.foodfinder11.dto.NoData
import com.example.foodfinder11.dto.RegisterRequestDto
import com.example.foodfinder11.dto.RegisterResponseDto
import com.example.foodfinder11.dto.RegisterRestaurantRequestDto
import com.example.foodfinder11.dto.RegisterRestaurantResponseDto
import com.example.foodfinder11.dto.ResponseWrapper
import com.example.foodfinder11.dto.SaveMealRequestDto
import com.example.foodfinder11.dto.SaveMealResponseDto
import com.example.foodfinder11.dto.SavePromotionRequestDto
import com.example.foodfinder11.dto.SaveRestaurantLocationRequestDto
import com.example.foodfinder11.dto.SaveRestaurantRequestDto
import com.example.foodfinder11.model.FoodType
import com.example.foodfinder11.model.Meal
import com.example.foodfinder11.model.Order
import com.example.foodfinder11.model.Restaurant
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST


interface APItiteService {

    @POST("/api/login")
    fun login(@Body requestData: LoginRequestDto) : Call<ResponseWrapper<LoginResponseDto>>

    @POST("/api/checkIfEmailExists")
    fun checkIfEmailExists(@Body requestData: CheckIfEmailExistsRequestDto) : Call<ResponseWrapper<CheckIfEmailExistsResponseDto>>

    @POST("/api/register")
    fun register(@Body requestData: RegisterRequestDto) : Call<ResponseWrapper<RegisterResponseDto>>

    @GET("/api/restaurants/get-all")
    fun getAllRestaurants() : Call<ResponseWrapper<List<Restaurant>>>

    @POST("/api/restaurants/get_by_owner_id")
    fun getByOwnerId(@Body dto: IdentifierDto) : Call<ResponseWrapper<Restaurant?>>

    @POST("/api/restaurants/get_by_id")
    fun getRestaurantById(@Body dto: IdentifierDto) : Call<ResponseWrapper<Restaurant?>>

    @POST("/api/restaurants/register")
    fun registerRestaurant(@Body requestData: RegisterRestaurantRequestDto): Call<ResponseWrapper<RegisterRestaurantResponseDto>>

    @POST("/api/restaurants/save")
    fun saveRestaurant(@Body requestData: SaveRestaurantRequestDto): Call<ResponseWrapper<Restaurant>>

    @POST("/api/restaurants/save_location")
    fun saveRestaurantLocation( @Body requestData: SaveRestaurantLocationRequestDto): Call<ResponseWrapper<NoData>>

    @POST("/api/restaurants/addRemoveFavoriteRestaurant")
    fun addRemoveFavoriteRestaurant( @Body requestData: AddRemoveFavoriteRestaurantRequestDto): Call<ResponseWrapper<List<Restaurant>>>

    @GET("/api/food_types/get-all")
    fun getAllFoodTypes() : Call<ResponseWrapper<List<FoodType>>>

    @POST("/api/meals/get_meals")
    fun getMeals(@Body dto: IdentifierDto) : Call<ResponseWrapper<List<Meal>>>

    @POST("/api/meals/save_meal")
    fun saveMeal( @Body requestData: SaveMealRequestDto): Call<ResponseWrapper<SaveMealResponseDto>>

    @HTTP(method = "DELETE", path = "/api/meals/delete_meal", hasBody = true)
    fun deleteMeal( @Body dto: IdentifierDto): Call<ResponseWrapper<NoData>>

    @POST("/api/orders/confirm")
    fun confirmOrder( @Body requestData: Order): Call<ResponseWrapper<NoData>>
}