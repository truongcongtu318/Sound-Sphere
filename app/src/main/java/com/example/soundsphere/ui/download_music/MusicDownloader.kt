package com.example.soundsphere.ui.download_music

import android.content.Context
import android.os.Environment
import android.widget.Toast
import androidx.annotation.OptIn
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

class MusicDownloader @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val client = OkHttpClient()

    @OptIn(UnstableApi::class)
    suspend fun downloadMusic(url: String, fileName: String) {
        withContext(Dispatchers.IO) {
            try {
                val request = Request.Builder().url(url).build()
                val response = client.newCall(request).execute()
                if (!response.isSuccessful) throw Exception("Failed to download file: $response")

                val filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath
                Log.d("MusicDownloader", "Downloaded to $filePath/$fileName")
                val file = File(filePath, fileName)
                val inputStream: InputStream? = response.body?.byteStream()
                val outputStream: OutputStream = FileOutputStream(file)
                inputStream?.copyTo(outputStream)
                outputStream.close()
                inputStream?.close()

                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Downloaded to $filePath/$fileName", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("MusicDownloader", "Error downloading music: ${e.message}")
                    Toast.makeText(context, "Download failed: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
