package com.tecsup.pokemon

import android.content.Context
import com.tecsup.pokemon.data.auth.AuthRepository
import com.tecsup.pokemon.data.auth.FirebaseAuthRepository
import com.tecsup.pokemon.data.local.AppDatabase
import com.tecsup.pokemon.data.remote.ApiService
import com.tecsup.pokemon.data.remote.RetrofitClient
import com.tecsup.pokemon.data.repository.ItemRepository
import com.tecsup.pokemon.data.repository.ItemRepositoryImpl
import com.tecsup.pokemon.notification.Notificador
import com.tecsup.pokemon.notification.NotificadorAndroid

// Crea la API, la base de datos y los repositorios una sola vez.
class AppContainer private constructor(context: Context) {

    private val api: ApiService = RetrofitClient.api
    private val database: AppDatabase = AppDatabase.crear(context)

    val itemRepository: ItemRepository = ItemRepositoryImpl(api, database.itemDao())
    val authRepository: AuthRepository = FirebaseAuthRepository()
    val notificador: Notificador = NotificadorAndroid(context)

    companion object {
        @Volatile
        private var instancia: AppContainer? = null

        // Una sola instancia para toda la app (también al girar la pantalla).
        fun obtener(context: Context): AppContainer =
            instancia ?: synchronized(this) {
                instancia ?: AppContainer(context.applicationContext).also { instancia = it }
            }
    }
}
