package com.example.foodfinder11.dto

data class ResponseWrapper<T>(val message: String, val status: Int, val data: T?)