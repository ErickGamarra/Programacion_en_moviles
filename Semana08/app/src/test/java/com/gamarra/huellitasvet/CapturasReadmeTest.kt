package com.gamarra.huellitasvet

import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import java.io.File
import java.time.LocalDate

// Recorre la app real (Room + datos iniciales) y guarda las capturas del README en Semana08/capturas
@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [35], qualifiers = "w411dp-h891dp-port-xhdpi")
class CapturasReadmeTest {
    @get:Rule
    val regla = createAndroidComposeRule(MainActivity::class.java, effectContext = Dispatchers.Main)

    private val carpeta = File(System.getProperty("user.dir"), "../capturas").apply { mkdirs() }

    @Test
    fun generarCapturas() {
        esperarDatos()

        ingresar("ana@correo.test", "1234")
        esperar("Mis mascotas")
        esperar("Próxima cita")
        capturar("1_dueno_inicio")

        regla.onNodeWithText("Luna").performClick()
        esperar("Historial clínico")
        regla.onNodeWithText("Historial clínico").performClick()
        esperar("Tratamiento:", parcial = true)
        capturar("2_dueno_ficha_historial")

        regla.onNodeWithText("Pedir cita").performClick()
        esperar("Consulta general")
        regla.onNodeWithText("Consulta general").performClick()
        regla.onNodeWithText(LocalDate.now().plusDays(1).dayOfMonth.toString()).performClick()
        regla.waitForIdle()
        regla.onNodeWithText("10:00").performClick()
        regla.waitForIdle()
        capturar("3_dueno_pedir_cita")

        atras()
        atras()
        esperar("Perfil")
        regla.onNodeWithText("Perfil").performClick()
        esperar("Cambiar de perfil")
        regla.onNodeWithText("Cambiar de perfil").performClick()

        esperar("Ingresar")
        ingresar("vet@huellitas.test", "admin")
        esperar("Panel de la clínica")
        esperar("Ingresos del mes")
        capturar("4_vet_panel")

        regla.onNodeWithText("Agenda").performClick()
        esperar("Agenda")
        runCatching { esperar("Confirmar", alternativa = "Atender") }
        capturar("5_vet_agenda")

        regla.onNodeWithText("Pacientes").performClick()
        esperar("Rocky")
        capturar("6_vet_pacientes")
    }

    private fun esperarDatos() = runBlocking {
        val app = ApplicationProvider.getApplicationContext<VetApp>()
        val limite = System.currentTimeMillis() + 15_000
        while (app.database.usuarioDao().contar() == 0 && System.currentTimeMillis() < limite) Thread.sleep(100)
    }

    private fun ingresar(correo: String, clave: String) {
        esperar("Ingresar")
        regla.onNodeWithText("Correo").performTextInput(correo)
        regla.onNodeWithText("Contraseña").performTextInput(clave)
        regla.onNodeWithText("Ingresar").performClick()
    }

    private fun esperar(texto: String, parcial: Boolean = false, alternativa: String? = null) {
        regla.waitUntil(15_000) {
            regla.onAllNodesWithText(texto, substring = parcial).fetchSemanticsNodes().isNotEmpty() ||
                (alternativa != null && regla.onAllNodesWithText(alternativa).fetchSemanticsNodes().isNotEmpty())
        }
    }

    private fun atras() {
        regla.runOnUiThread { regla.activity.onBackPressedDispatcher.onBackPressed() }
        regla.waitForIdle()
    }

    private fun capturar(nombre: String) {
        regla.waitForIdle()
        regla.mainClock.advanceTimeBy(500)
        val vista = regla.activity.window.decorView
        val imagen = Bitmap.createBitmap(vista.width, vista.height, Bitmap.Config.ARGB_8888)
        regla.runOnUiThread { vista.draw(Canvas(imagen)) }
        File(carpeta, "$nombre.png").outputStream().use { imagen.compress(Bitmap.CompressFormat.PNG, 100, it) }
    }
}
