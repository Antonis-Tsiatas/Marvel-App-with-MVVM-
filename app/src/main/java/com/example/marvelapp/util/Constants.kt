package com.example.marvelapp.util

import java.math.BigInteger
import java.security.MessageDigest
import java.sql.Timestamp

object Constants {


        const val BASE_URL = "https://gateway.marvel.com/"
        val timeStamp = Timestamp(System.currentTimeMillis()).time.toString()
        val API_KEY = "d36b55d41979a346fb742251e763f1c8"
        val PRIVATE_KEY = "0f103f3249a9849e3c203efbf5253ae31550e320"
        fun hash():String{
            val input ="$timeStamp$PRIVATE_KEY$API_KEY"
           // Log.d("inputhash",input)
            val md = MessageDigest.getInstance("MD5")
            return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')

        }

}