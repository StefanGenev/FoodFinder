package com.example.foodfinder11.model

enum class Roles {

    CUSTOMER {
        override fun getName(): String {
            return "Customer"
        }
    },

    RESTAURANT {
        override fun getName(): String {
            return "Owner"
        }
    },

    ADMIN {
        override fun getName(): String {
            return "Admin"
        }
    };

    abstract fun getName(): String
}