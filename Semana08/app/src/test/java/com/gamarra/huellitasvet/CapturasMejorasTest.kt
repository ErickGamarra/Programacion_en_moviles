package com.gamarra.huellitasvet

import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Looper
import android.view.View
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTextReplacement
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import java.io.File
import java.time.LocalDate

// Recorre la app real y guarda las capturas de las mejoras de la rama de IA en Semana08/capturas/mejora-ia
@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [35], qualifiers = "w411dp-h891dp-port-xhdpi")
class CapturasMejorasTest {
    @get:Rule
    val regla = createAndroidComposeRule(MainActivity::class.java, effectContext = Dispatchers.Main)

    private val carpeta = File(System.getProperty("user.dir"), "../capturas/mejora-ia").apply { mkdirs() }

    @Test
    fun generarCapturas() {
        esperarDatos()

        ingresar("ana@correo.test", "1234")
        esperar("Mis mascotas")
        esperar("Luna")
        regla.onNodeWithText("Luna").performClick()
        esperar("Historial clínico")
        regla.onNodeWithText("Historial clínico").performClick()
        esperar("Evolución del peso (kg)")
        capturar("A_grafico_peso")

        atras()
        esperar("Citas")
        regla.onNodeWithText("Citas").performClick()
        esperar("Reprogramar")
        capturar("B1_mis_citas_reprogramar")
        regla.onAllNodesWithText("Reprogramar")[0].performClick()
        esperar("Cita actual")
        regla.onNodeWithText(LocalDate.now().plusDays(3).dayOfMonth.toString()).performClick()
        regla.waitForIdle()
        regla.onNodeWithText("11:00").performClick()
        esperar("Nuevo horario:", parcial = true)
        capturar("B2_reprogramar_cita")
        regla.onNodeWithText("Confirmar cambio").performClick()
        esperar("Mis citas")
        esperar("11:00", parcial = true)
        capturar("B3_cita_reprogramada")

        regla.onNodeWithText("Inicio").performClick()
        esperar("Luna")
        regla.onNodeWithText("Luna").performClick()
        esperar("Vacunas")
        regla.onNodeWithText("Vacunas").performClick()
        esperar("Antirrábica")
        capturar("C1_carnet_vacunas")
        regla.onNodeWithText("Registrar vacuna").performClick()
        esperar("Guardar")
        regla.onNodeWithText("Vacuna").performTextInput("Le")
        regla.onNodeWithText("Fecha aplicada (aaaa-mm-dd)").performTextReplacement(LocalDate.now().plusDays(5).toString())
        regla.onNodeWithText("Guardar").performClick()
        esperar("Mínimo 3 caracteres")
        capturar("C2_vacuna_validaciones")
        regla.onNodeWithText("Vacuna").performTextReplacement("Leucemia felina")
        regla.onNodeWithText("Fecha aplicada (aaaa-mm-dd)").performTextReplacement(LocalDate.now().toString())
        regla.onNodeWithText("Próxima dosis (opcional)").performTextInput(LocalDate.now().plusYears(1).toString())
        regla.onNodeWithText("Guardar").performClick()
        esperar("Leucemia felina")
        capturar("C3_vacuna_registrada")
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

    private fun avanzar() {
        repeat(10) {
            regla.mainClock.advanceTimeBy(100)
            Shadows.shadowOf(Looper.getMainLooper()).idle()
            Thread.sleep(50)
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun ventanas(): List<View> {
        val clase = Class.forName("android.view.WindowManagerGlobal")
        val global = clase.getMethod("getInstance").invoke(null)
        val campo = clase.getDeclaredField("mViews").apply { isAccessible = true }
        return (campo.get(global) as List<View>).filter { it.isShown }
    }

    private fun capturar(nombre: String) {
        if (regla.mainClock.autoAdvance) {
            regla.waitForIdle()
            regla.mainClock.advanceTimeBy(500)
        } else {
            avanzar()
        }
        val vista = regla.activity.window.decorView
        val imagen = Bitmap.createBitmap(vista.width, vista.height, Bitmap.Config.ARGB_8888)
        regla.runOnUiThread {
            val lienzo = Canvas(imagen)
            ventanas().forEachIndexed { i, v ->
                if (i > 0) lienzo.drawColor(0x99000000.toInt())
                val pos = IntArray(2).also { v.getLocationOnScreen(it) }
                lienzo.save()
                lienzo.translate(pos[0].toFloat(), pos[1].toFloat())
                v.draw(lienzo)
                lienzo.restore()
            }
        }
        File(carpeta, "$nombre.png").outputStream().use { imagen.compress(Bitmap.CompressFormat.PNG, 100, it) }
    }
}
