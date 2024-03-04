package com.example.foodfinder11.utils

import android.content.Context
import android.net.Uri
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback


class CloudinaryManager {
    companion object {
        private var mediaManager: Any? = null

        fun startMediaManager(context: Context){
            if (mediaManager == null){
                mediaManager = initialize(context)
            }
        }

        // Initialize Cloudinary with cloud name, API key, and API secret
        fun initialize(context: Context) {
            val config: MutableMap<String, String> = HashMap()
            config["cloud_name"] = "test"
            config["api_key"] = "test"
            config["api_secret"] = "test"

            MediaManager.init(context, config)
        }

        // Method to upload an image to Cloudinary
        fun uploadImage(imageUri: Uri, listener: OnUploadListener) {

            MediaManager.get().upload(imageUri)
                .callback(object : UploadCallback {
                    override fun onStart(requestId: String) {
                        // Upload has started
                    }

                    override fun onProgress(requestId: String?, bytes: Long, totalBytes: Long) {
                        // Upload progress
                    }

                    override fun onSuccess(requestId: String?, resultData: MutableMap<Any?, Any?>?) {
                        // Image uploaded successfully, retrieve the URL
                        val imageUrl = resultData?.get("url") as String
                        listener.onUploadSuccess(imageUrl)
                    }

                    override fun onError(requestId: String?, error: ErrorInfo?) {
                        // Upload error
                        listener.onUploadError(error?.description ?: "Unknown error")
                    }

                    override fun onReschedule(requestId: String?, error: ErrorInfo?) {
                        // Retry uploading
                    }
                }).dispatch()
        }
    }

    // Interface for upload callbacks
    interface OnUploadListener {
        fun onUploadSuccess(imageUrl: String)
        fun onUploadError(error: String)
    }
}