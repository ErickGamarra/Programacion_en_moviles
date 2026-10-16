package com.gamarra.huellitasvet.data.prefs

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "ajustes")

class PreferenciasStore(private val context: Context) {
    private val perfilKey = stringPreferencesKey("perfilActivo")
    private val usuarioKey = intPreferencesKey("usuarioId")
    private val modoOscuroKey = booleanPreferencesKey("modoOscuro")
    private val notificacionesKey = booleanPreferencesKey("notificaciones")
    private val aceptarCitasKey = booleanPreferencesKey("aceptarCitas")

    val perfilActivo: Flow<String> = context.dataStore.data.map { it[perfilKey] ?: "" }
    val usuarioId: Flow<Int> = context.dataStore.data.map { it[usuarioKey] ?: 0 }
    val modoOscuro: Flow<Boolean> = context.dataStore.data.map { it[modoOscuroKey] ?: false }
    val notificaciones: Flow<Boolean> = context.dataStore.data.map { it[notificacionesKey] ?: true }
    val aceptarCitas: Flow<Boolean> = context.dataStore.data.map { it[aceptarCitasKey] ?: true }

    suspend fun guardarSesion(rol: String, usuarioId: Int) {
        context.dataStore.edit {
            it[perfilKey] = rol
            it[usuarioKey] = usuarioId
        }
    }

    suspend fun guardarModoOscuro(valor: Boolean) {
        context.dataStore.edit { it[modoOscuroKey] = valor }
    }

    suspend fun guardarNotificaciones(valor: Boolean) {
        context.dataStore.edit { it[notificacionesKey] = valor }
    }

    suspend fun guardarAceptarCitas(valor: Boolean) {
        context.dataStore.edit { it[aceptarCitasKey] = valor }
    }

    suspend fun cerrarSesion() {
        context.dataStore.edit {
            it.remove(perfilKey)
            it.remove(usuarioKey)
        }
    }
}
