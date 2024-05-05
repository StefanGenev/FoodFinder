package com.example.foodfinder11.utils

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import com.intuit.sdp.BuildConfig
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.zip.DataFormatException
import java.util.zip.Deflater
import java.util.zip.Inflater

object ImageUtils {
    fun compress(input: ByteArray?): ByteArray {
        val deflater = Deflater()
        deflater.setInput(input)
        deflater.finish()

        val outputStream = ByteArrayOutputStream()
        val buffer = ByteArray(1024)

        while (!deflater.finished()) {
            val compressedSize = deflater.deflate(buffer)
            outputStream.write(buffer, 0, compressedSize)
        }

        return outputStream.toByteArray()
    }


    @Throws(DataFormatException::class)
    fun decompress(input: ByteArray?): ByteArray {
        val inflater = Inflater()
        inflater.setInput(input)

        val outputStream = ByteArrayOutputStream()
        val buffer = ByteArray(1024)

        while (!inflater.finished()) {
            val decompressedSize = inflater.inflate(buffer)
            outputStream.write(buffer, 0, decompressedSize)
        }

        return outputStream.toByteArray()
    }

    fun Context.createTempPictureUri(
        provider: String = "${BuildConfig.APPLICATION_ID}.provider",
        fileName: String = "picture_${System.currentTimeMillis()}",
        fileExtension: String = ".png"
    ): Uri {
        val tempFile = File.createTempFile(
            fileName, fileExtension, cacheDir
        ).apply {
            createNewFile()
        }

        return FileProvider.getUriForFile(applicationContext, provider, tempFile)
    }
}