package com.example.foodfinder11

import com.example.foodfinder11.dto.AddRemoveFavoriteRestaurantRequestDto
import com.example.foodfinder11.dto.AddReviewRequestDto
import com.example.foodfinder11.dto.ChangeRestaurantStatusRequestDto
import com.example.foodfinder11.dto.CheckIfEmailExistsRequestDto
import com.example.foodfinder11.dto.CheckIfEmailExistsResponseDto
import com.example.foodfinder11.dto.ConfirmOrderRequestDto
import com.example.foodfinder11.dto.ConfirmOrderResponseDto
import com.example.foodfinder11.dto.IdentifierDto
import com.example.foodfinder11.dto.LoginRequestDto
import com.example.foodfinder11.dto.LoginResponseDto
import com.example.foodfinder11.dto.NoData
import com.example.foodfinder11.dto.RegisterRequestDto
import com.example.foodfinder11.dto.RegisterResponseDto
import com.example.foodfinder11.dto.RegisterRestaurantRequestDto
import com.example.foodfinder11.dto.RegisterRestaurantResponseDto
import com.example.foodfinder11.dto.ResponseWrapper
import com.example.foodfinder11.dto.RestaurantDetailsResponseDto
import com.example.foodfinder11.dto.SaveMealRequestDto
import com.example.foodfinder11.dto.SaveMealResponseDto
import com.example.foodfinder11.dto.SaveRestaurantLocationRequestDto
import com.example.foodfinder11.dto.SaveRestaurantRequestDto
import com.example.foodfinder11.model.FoodType
import com.example.foodfinder11.model.Meal
import com.example.foodfinder11.model.Order
import com.example.foodfinder11.model.Restaurant
import com.example.foodfinder11.model.Review
import com.example.foodfinder11.model.User
import com.example.foodfinder11.retrofit.APItiteService
import com.example.foodfinder11.retrofit.RetrofitInstance
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.test.assertEquals

class APItiteServiceTest {

    private lateinit var server: MockWebServer
    private lateinit var api: APItiteService

    @BeforeEach
    fun beforeEach() {

        server = MockWebServer()

        api = Retrofit.Builder()
            .baseUrl(server.url("/${RetrofitInstance.API_BASE_URL}"))
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(APItiteService::class.java)
    }

    @AfterEach
    fun afterEach() {
        server.shutdown()
    }

    @Test
    fun `login, returns Success`() = run {

        val dto = ResponseWrapper<LoginResponseDto>()
        val gson: Gson = GsonBuilder().create()
        val json = gson.toJson(dto)!!

        val res = MockResponse()
        res.setBody(json)
        server.enqueue(res)

        val data = RetrofitInstance.getApiService().login(LoginRequestDto()).execute()
        server.takeRequest()

        assertEquals(data.body()!!, dto)
    }

    @Test
    fun `checkIfEmailExists, returns Success`() = run {

        val dto = ResponseWrapper<CheckIfEmailExistsResponseDto>()
        val gson: Gson = GsonBuilder().create()
        val json = gson.toJson(dto)!!

        val res = MockResponse()
        res.setBody(json)
        server.enqueue(res)

        val data = api.checkIfEmailExists(CheckIfEmailExistsRequestDto()).execute()
        server.takeRequest()

        assertEquals(data.body()!!, dto)
    }

    @Test
    fun `register, returns Success`() = run {

        val dto = ResponseWrapper<RegisterResponseDto>()
        val gson: Gson = GsonBuilder().create()
        val json = gson.toJson(dto)!!

        val res = MockResponse()
        res.setBody(json)
        server.enqueue(res)

        val data = api.register(RegisterRequestDto()).execute()
        server.takeRequest()

        assertEquals(data.body()!!, dto)
    }

    @Test
    fun `getAllRestaurants, returns Success`() = run {

        val dto = ResponseWrapper<List<Restaurant>>()
        val gson: Gson = GsonBuilder().create()
        val json = gson.toJson(dto)!!

        val res = MockResponse()
        res.setBody(json)
        server.enqueue(res)

        val data = api.getAllRestaurants().execute()
        server.takeRequest()

        assertEquals(data.body()!!, dto)
    }

    @Test
    fun `getAllVisibleRestaurants, returns Success`() = run {

        val dto = ResponseWrapper<List<Restaurant>>()
        val gson: Gson = GsonBuilder().create()
        val json = gson.toJson(dto)!!

        val res = MockResponse()
        res.setBody(json)
        server.enqueue(res)

        val data = api.getAllVisibleRestaurants().execute()
        server.takeRequest()

        assertEquals(data.body()!!, dto)
    }

    @Test
    fun `getByOwnerId, returns Success`() = run {

        val dto = ResponseWrapper<RestaurantDetailsResponseDto>()
        val gson: Gson = GsonBuilder().create()
        val json = gson.toJson(dto)!!

        val res = MockResponse()
        res.setBody(json)
        server.enqueue(res)

        val data = api.getByOwnerId(IdentifierDto()).execute()
        server.takeRequest()

        assertEquals(data.body()!!, dto)
    }

    @Test
    fun `getRestaurantById, returns Success`() = run {

        val dto = ResponseWrapper<RestaurantDetailsResponseDto>()
        val gson: Gson = GsonBuilder().create()
        val json = gson.toJson(dto)!!

        val res = MockResponse()
        res.setBody(json)
        server.enqueue(res)

        val data = api.getRestaurantById(IdentifierDto()).execute()
        server.takeRequest()

        assertEquals(data.body()!!, dto)
    }

    @Test
    fun `registerRestaurant, returns Success`() = run {

        val dto = ResponseWrapper<RegisterRestaurantResponseDto>()
        val gson: Gson = GsonBuilder().create()
        val json = gson.toJson(dto)!!

        val res = MockResponse()
        res.setBody(json)
        server.enqueue(res)

        val data = api.registerRestaurant(RegisterRestaurantRequestDto()).execute()
        server.takeRequest()

        assertEquals(data.body()!!, dto)
    }

    @Test
    fun `saveRestaurant, returns Success`() = run {

        val dto = ResponseWrapper<Restaurant>()
        val gson: Gson = GsonBuilder().create()
        val json = gson.toJson(dto)!!

        val res = MockResponse()
        res.setBody(json)
        server.enqueue(res)

        val data = api.saveRestaurant(SaveRestaurantRequestDto()).execute()
        server.takeRequest()

        assertEquals(data.body()!!, dto)
    }

    @Test
    fun `changeRestaurantStatus, returns Success`() = run {

        val dto = ResponseWrapper<NoData>()
        val gson: Gson = GsonBuilder().create()
        val json = gson.toJson(dto)!!

        val res = MockResponse()
        res.setBody(json)
        server.enqueue(res)

        val data = api.changeRestaurantStatus(ChangeRestaurantStatusRequestDto()).execute()
        server.takeRequest()

        assertEquals(data.body()!!, dto)
    }

    @Test
    fun `saveRestaurantLocation, returns Success`() = run {

        val dto = ResponseWrapper<NoData>()
        val gson: Gson = GsonBuilder().create()
        val json = gson.toJson(dto)!!

        val res = MockResponse()
        res.setBody(json)
        server.enqueue(res)

        val data = api.saveRestaurantLocation(SaveRestaurantLocationRequestDto()).execute()
        server.takeRequest()

        assertEquals(data.body()!!, dto)
    }

    @Test
    fun `addRemoveFavoriteRestaurant, returns Success`() = run {

        val dto = ResponseWrapper<List<Restaurant>>()
        val gson: Gson = GsonBuilder().create()
        val json = gson.toJson(dto)!!

        val res = MockResponse()
        res.setBody(json)
        server.enqueue(res)

        val data = api.addRemoveFavoriteRestaurant(AddRemoveFavoriteRestaurantRequestDto()).execute()
        server.takeRequest()

        assertEquals(data.body()!!, dto)
    }

    @Test
    fun `deleteRestaurant, returns Success`() = run {

        val dto = ResponseWrapper<NoData>()
        val gson: Gson = GsonBuilder().create()
        val json = gson.toJson(dto)!!

        val res = MockResponse()
        res.setBody(json)
        server.enqueue(res)

        val data = api.deleteRestaurant(IdentifierDto()).execute()
        server.takeRequest()

        assertEquals(data.body()!!, dto)
    }

    @Test
    fun `getAllFoodTypes, returns Success`() = run {

        val dto = ResponseWrapper<List<FoodType>>()
        val gson: Gson = GsonBuilder().create()
        val json = gson.toJson(dto)!!

        val res = MockResponse()
        res.setBody(json)
        server.enqueue(res)

        val data = api.getAllFoodTypes().execute()
        server.takeRequest()

        assertEquals(data.body()!!, dto)
    }

    @Test
    fun `getMeals, returns Success`() = run {

        val dto = ResponseWrapper<List<Meal>>()
        val gson: Gson = GsonBuilder().create()
        val json = gson.toJson(dto)!!

        val res = MockResponse()
        res.setBody(json)
        server.enqueue(res)

        val data = api.getMeals(IdentifierDto()).execute()
        server.takeRequest()

        assertEquals(data.body()!!, dto)
    }

    @Test
    fun `getVisibleMeals, returns Success`() = run {

        val dto = ResponseWrapper<List<Meal>>()
        val gson: Gson = GsonBuilder().create()
        val json = gson.toJson(dto)!!

        val res = MockResponse()
        res.setBody(json)
        server.enqueue(res)

        val data = api.getVisibleMeals(IdentifierDto()).execute()
        server.takeRequest()

        assertEquals(data.body()!!, dto)
    }

    @Test
    fun `saveMeal, returns Success`() = run {

        val dto = ResponseWrapper<SaveMealResponseDto>()
        val gson: Gson = GsonBuilder().create()
        val json = gson.toJson(dto)!!

        val res = MockResponse()
        res.setBody(json)
        server.enqueue(res)

        val data = api.saveMeal(SaveMealRequestDto()).execute()
        server.takeRequest()

        assertEquals(data.body()!!, dto)
    }

    @Test
    fun `deleteMeal, returns Success`() = run {

        val dto = ResponseWrapper<NoData>()
        val gson: Gson = GsonBuilder().create()
        val json = gson.toJson(dto)!!

        val res = MockResponse()
        res.setBody(json)
        server.enqueue(res)

        val data = api.deleteMeal(IdentifierDto()).execute()
        server.takeRequest()

        assertEquals(data.body()!!, dto)
    }

    @Test
    fun `confirmOrder, returns Success`() = run {

        val dto = ResponseWrapper<ConfirmOrderResponseDto>()
        val gson: Gson = GsonBuilder().create()
        val json = gson.toJson(dto)!!

        val res = MockResponse()
        res.setBody(json)
        server.enqueue(res)

        val data = api.confirmOrder(ConfirmOrderRequestDto()).execute()
        server.takeRequest()

        assertEquals(data.body()!!, dto)
    }

    @Test
    fun `getOrdersByUserId, returns Success`() = run {

        val dto = ResponseWrapper<List<Order>>()
        val gson: Gson = GsonBuilder().create()
        val json = gson.toJson(dto)!!

        val res = MockResponse()
        res.setBody(json)
        server.enqueue(res)

        val data = api.getOrdersByUserId(IdentifierDto()).execute()
        server.takeRequest()

        assertEquals(data.body()!!, dto)
    }

    @Test
    fun `getOrdersByRestaurantId, returns Success`() = run {

        val dto = ResponseWrapper<List<Order>>()
        val gson: Gson = GsonBuilder().create()
        val json = gson.toJson(dto)!!

        val res = MockResponse()
        res.setBody(json)
        server.enqueue(res)

        val data = api.getOrdersByRestaurantId(IdentifierDto()).execute()
        server.takeRequest()

        assertEquals(data.body()!!, dto)
    }

    @Test
    fun `getCustomers, returns Success`() = run {

        val dto = ResponseWrapper<List<User>>()
        val gson: Gson = GsonBuilder().create()
        val json = gson.toJson(dto)!!

        val res = MockResponse()
        res.setBody(json)
        server.enqueue(res)

        val data = api.getCustomers().execute()
        server.takeRequest()

        assertEquals(data.body()!!, dto)
    }

    @Test
    fun `deleteUser, returns Success`() = run {

        val dto = ResponseWrapper<NoData>()
        val gson: Gson = GsonBuilder().create()
        val json = gson.toJson(dto)!!

        val res = MockResponse()
        res.setBody(json)
        server.enqueue(res)

        val data = api.deleteUser(IdentifierDto()).execute()
        server.takeRequest()

        assertEquals(data.body()!!, dto)
    }

    @Test
    fun `getReviewsByRestaurantId, returns Success`() = run {

        val dto = ResponseWrapper<List<Review>>()
        val gson: Gson = GsonBuilder().create()
        val json = gson.toJson(dto)!!

        val res = MockResponse()
        res.setBody(json)
        server.enqueue(res)

        val data = api.getReviewsByRestaurantId(IdentifierDto()).execute()
        server.takeRequest()

        assertEquals(data.body()!!, dto)
    }

    @Test
    fun `getReviewsByUserId, returns Success`() = run {

        val dto = ResponseWrapper<List<Review>>()
        val gson: Gson = GsonBuilder().create()
        val json = gson.toJson(dto)!!

        val res = MockResponse()
        res.setBody(json)
        server.enqueue(res)

        val data = api.getReviewsByUserId(IdentifierDto()).execute()
        server.takeRequest()

        assertEquals(data.body()!!, dto)
    }

    @Test
    fun `addReview, returns Success`() = run {

        val dto = ResponseWrapper<NoData>()
        val gson: Gson = GsonBuilder().create()
        val json = gson.toJson(dto)!!

        val res = MockResponse()
        res.setBody(json)
        server.enqueue(res)

        val data = api.addReview(AddReviewRequestDto()).execute()
        server.takeRequest()

        assertEquals(data.body()!!, dto)
    }

}
