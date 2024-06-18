package com.example.foodfinder11

import com.example.foodfinder11.dto.CheckIfEmailExistsRequestDto
import com.example.foodfinder11.dto.CheckIfEmailExistsResponseDto
import com.example.foodfinder11.dto.ResponseWrapper
import com.example.foodfinder11.model.Restaurant
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
    fun `checkIfEmailExists, returns Success`() = run {

        val dto = ResponseWrapper<CheckIfEmailExistsResponseDto>() //The object I want back as response

        val gson: Gson = GsonBuilder().create()

        val json = gson.toJson(dto)!! //Convert the object into json string using GSON

        val res = MockResponse() //Make a fake response for our server call
        res.setBody(json) //set the body of the fake response as the json string you are expecting as a response
        server.enqueue(res) //add it in the server response queue

        val data = api.checkIfEmailExists(CheckIfEmailExistsRequestDto()).execute() //make the call to our fake server(as we are using fake base url)
        server.takeRequest()//let the server take the request

        assertEquals(data.body()!!, dto)//the data you are getting as the call response should be same
    }

}
