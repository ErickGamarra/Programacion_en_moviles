package com.tecsup.murocampus.data.remote

import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

// Un solo cliente para toda la app
object RetrofitClient {

    private const val BASE_URL = "https://jsonplaceholder.typicode.com/"

    // Si el servidor manda campos que no están en el modelo, se ignoran
    private val json = Json { ignoreUnknownKeys = true }

    // Muestra en Logcat (etiqueta OkHttp) cada petición y su respuesta (solo para desarrollo)
    private val cliente = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    val api: PostApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(cliente)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(PostApi::class.java)
    }
}
