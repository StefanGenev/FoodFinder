package com.example.foodfinder11.utils

import java.math.BigInteger
import java.security.MessageDigest

object HashingUtils {

    fun getSHA512(input:String): String {
        val md: MessageDigest = MessageDigest.getInstance("SHA-512")
        val messageDigest = md.digest(input.toByteArray())

        // Convert byte array into signum representation
        val no = BigInteger(1, messageDigest)

        // Convert message digest into hex value
        var hashtext: String = no.toString(16)

        // Add preceding 0s to make it 128 chars long
        while (hashtext.length < 128) {
            hashtext = "0$hashtext"
        }

        // return the HashText
        return hashtext
    }
}