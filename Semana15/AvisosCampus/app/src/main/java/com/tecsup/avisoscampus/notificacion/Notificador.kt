package com.tecsup.avisoscampus.notificacion

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.tecsup.avisoscampus.MainActivity
import com.tecsup.avisoscampus.R
import com.tecsup.avisoscampus.model.Aviso

object Notificador {
    const val CANAL = "avisos"
    const val CANAL_URGENTE = "urgentes"

    fun crearCanal(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val canal = NotificationChannel(CANAL, "Avisos", NotificationManager.IMPORTANCE_DEFAULT)
            canal.description = "Notificaciones de la app"
            // IMPORTANCE_HIGH hace que la notificación aparezca emergente arriba de la pantalla
            val urgente = NotificationChannel(CANAL_URGENTE, "Urgentes", NotificationManager.IMPORTANCE_HIGH)
            urgente.description = "Recordatorios que no debes perder"
            val manager = context.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannels(listOf(canal, urgente))
        }
    }

    fun tienePermiso(context: Context): Boolean {
        // Antes de Android 13 el permiso no existe: se concede al instalar
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return true
        return ContextCompat.checkSelfPermission(
            context, Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    }

    // El permiso se comprueba con tienePermiso antes de llamar a notify
    @SuppressLint("MissingPermission")
    fun mostrar(context: Context, aviso: Aviso): Boolean {
        if (!tienePermiso(context)) return false

        // Al tocar la notificación se abre MainActivity
        val intent = Intent(context, MainActivity::class.java)
        val pendiente = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        // El canal lo decide el aviso: el ViewModel marca como urgente al recordatorio
        val canal = if (aviso.urgente) CANAL_URGENTE else CANAL
        val prioridad = if (aviso.urgente) NotificationCompat.PRIORITY_HIGH else NotificationCompat.PRIORITY_DEFAULT

        val notificacion = NotificationCompat.Builder(context, canal)
            .setSmallIcon(R.drawable.ic_notificacion)
            .setContentTitle(aviso.titulo)
            .setContentText(aviso.texto)
            .setPriority(prioridad)
            .setContentIntent(pendiente)
            .setAutoCancel(true)   // se quita del panel al tocarla
            .build()

        // Cada aviso usa su propio id, así no se reemplazan entre ellos
        NotificationManagerCompat.from(context).notify(aviso.id, notificacion)
        return true
    }
}
