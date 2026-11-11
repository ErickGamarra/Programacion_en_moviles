package com.tecsup.pokemon.ui.lista

import com.tecsup.pokemon.domain.model.Item
import com.tecsup.pokemon.fake.NotificadorFalso
import com.tecsup.pokemon.fake.RepositorioFalso
import com.tecsup.pokemon.ui.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class ListaViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    private val charmander = Item(4, "charmander", "#004", null)
    private val vulpix = Item(37, "vulpix", "#037", null)

    @Before
    fun setUp() = Dispatchers.setMain(dispatcher)

    @After
    fun tearDown() = Dispatchers.resetMain()

    @Test
    fun `al refrescar con exito muestra la lista y notifica cuantos se guardaron`() = runTest(dispatcher) {
        val repo = RepositorioFalso().apply { respuestaApi = Result.success(listOf(charmander, vulpix)) }
        val notificador = NotificadorFalso()

        val vm = ListaViewModel(repo, notificador)
        advanceUntilIdle()

        val estado = vm.estado.value
        assertTrue(estado is UiState.Exito)
        assertEquals(2, (estado as UiState.Exito).datos.size)
        assertEquals("Se guardaron 2 elementos", notificador.mostrados.single().second)
        assertNull(vm.aviso.value)
    }

    @Test
    fun `sin internet y sin datos guardados muestra error con mensaje claro`() = runTest(dispatcher) {
        val repo = RepositorioFalso().apply { respuestaApi = Result.failure(IOException()) }
        val notificador = NotificadorFalso()

        val vm = ListaViewModel(repo, notificador)
        advanceUntilIdle()

        assertEquals(UiState.Error("No hay conexión a internet"), vm.estado.value)
        assertTrue(notificador.mostrados.isEmpty())
    }

    @Test
    fun `sin internet pero con datos guardados muestra la lista y el aviso`() = runTest(dispatcher) {
        val repo = RepositorioFalso(guardados = listOf(charmander)).apply {
            respuestaApi = Result.failure(IOException())
        }

        val vm = ListaViewModel(repo, NotificadorFalso())
        advanceUntilIdle()

        assertEquals(UiState.Exito(listOf(charmander)), vm.estado.value)
        assertEquals("Sin conexión: mostrando datos guardados", vm.aviso.value)
    }

    @Test
    fun `error del servidor muestra el codigo HTTP`() = runTest(dispatcher) {
        val error = HttpException(Response.error<Any>(500, "".toResponseBody()))
        val repo = RepositorioFalso().apply { respuestaApi = Result.failure(error) }

        val vm = ListaViewModel(repo, NotificadorFalso())
        advanceUntilIdle()

        assertEquals(UiState.Error("El servidor respondió con error 500"), vm.estado.value)
    }

    @Test
    fun `los favoritos se conservan al volver a sincronizar`() = runTest(dispatcher) {
        val repo = RepositorioFalso().apply { respuestaApi = Result.success(listOf(charmander, vulpix)) }
        val vm = ListaViewModel(repo, NotificadorFalso())
        advanceUntilIdle()

        vm.alternarFavorito(4)
        advanceUntilIdle()
        vm.refrescar()
        advanceUntilIdle()

        val lista = (vm.estado.value as UiState.Exito).datos
        assertTrue(lista.first { it.id == 4 }.esFavorito)
        assertEquals(2, repo.llamadasRefrescar)
    }

    @Test
    fun `reintentar despues de un error vuelve a cargar la lista`() = runTest(dispatcher) {
        val repo = RepositorioFalso().apply { respuestaApi = Result.failure(IOException()) }
        val vm = ListaViewModel(repo, NotificadorFalso())
        advanceUntilIdle()
        assertTrue(vm.estado.value is UiState.Error)

        repo.respuestaApi = Result.success(listOf(vulpix))
        vm.refrescar()
        advanceUntilIdle()

        assertEquals(UiState.Exito(listOf(vulpix)), vm.estado.value)
    }
}
