package com.tecsup.avisoscampus.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.tecsup.avisoscampus.model.Aviso
import com.tecsup.avisoscampus.model.Origen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Guarda el historial en DataStore para que no se pierda al cerrar la app.
// No recibe Context: el DataStore se crea en AvisosApp y llega ya armado.
class AvisosRepositoryDataStore(
    private val almacen: DataStore<Preferences>,
    private val alcance: CoroutineScope
) : AvisosRepository {

    private val _avisos = MutableStateFlow<List<Aviso>>(emptyList())
    override val avisos: StateFlow<List<Aviso>> = _avisos.asStateFlow()

    init {
        // Al iniciar se lee lo guardado (fuera del hilo principal)
        alcance.launch {
            val guardado = almacen.data.first()[CLAVE] ?: return@launch
            val lista = desdeJson(guardado)
            _avisos.update { actuales -> actuales + lista }
        }
    }

    override fun registrar(titulo: String, texto: String, origen: Origen): Aviso {
        val hora = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
        // El id sigue al mayor guardado, así no se repite después de reabrir la app
        val id = (_avisos.value.maxOfOrNull { it.id } ?: 0) + 1
        val aviso = Aviso(id, titulo, texto, origen, hora)
        _avisos.update { listOf(aviso) + it }          // lo más reciente arriba
        guardar(_avisos.value.take(MAXIMO))
        return aviso
    }

    private fun guardar(lista: List<Aviso>) {
        alcance.launch {
            almacen.edit { preferencias -> preferencias[CLAVE] = aJson(lista) }
        }
    }

    private fun aJson(lista: List<Aviso>): String {
        val arreglo = JSONArray()
        lista.forEach { aviso ->
            arreglo.put(
                JSONObject()
                    .put("id", aviso.id)
                    .put("titulo", aviso.titulo)
                    .put("texto", aviso.texto)
                    .put("origen", aviso.origen.name)
                    .put("hora", aviso.hora)
            )
        }
        return arreglo.toString()
    }

    private fun desdeJson(json: String): List<Aviso> {
        val arreglo = JSONArray(json)
        return List(arreglo.length()) { i ->
            val o = arreglo.getJSONObject(i)
            Aviso(
                id = o.getInt("id"),
                titulo = o.getString("titulo"),
                texto = o.getString("texto"),
                origen = Origen.valueOf(o.getString("origen")),
                hora = o.getString("hora")
            )
        }
    }

    private companion object {
        val CLAVE = stringPreferencesKey("historial")
        const val MAXIMO = 50   // no hace falta guardar más que eso
    }
}
