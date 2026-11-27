# PROMPTS · Fase 2 (rama mejora-ia-avisoscampus)

Mejoras elegidas de la tabla de la guía: **Dos canales**, **Texto largo** e **Historial persistente**. La última cambia el Repository y la primera el ViewModel, como piden las reglas. Cada mejora tiene su propio commit.

| Prompt que escribí | Qué respondió la IA | Qué cambié o descarté | Por qué |
|---|---|---|---|
| «Agrega a AvisosCampus un segundo canal Urgentes con IMPORTANCE_HIGH, aparte de Avisos, y haz que el recordatorio use el urgente. Respeta MVVM: el ViewModel no puede usar Context ni NotificationManager.» | Crear los dos canales en `crearCanal` con `createNotificationChannels` y elegir el canal en `Notificador.mostrar` según el aviso. | Descarté elegir el canal según `aviso.origen == RECORDATORIO` dentro del Notificador. Agregué `urgente: Boolean = false` a `Aviso` y el ViewModel emite `aviso.copy(urgente = true)` en `recordarEn`. | El Notificador no debe decidir qué es importante, eso le toca al ViewModel. Así el ViewModel decide sin tocar el sistema y el Notificador solo lee el dato. `urgente` no se guarda en el historial porque solo sirve para mostrar. |
| «Haz que al expandir la notificación se vea el texto completo con BigTextStyle.» | Un `NotificationCompat.BigTextStyle().bigText(...)` pasado al Builder con `setStyle`. | Como los textos de la guía son cortos, el texto expandido arma un mensaje más largo con el origen, la hora y una indicación para abrir la app. Agregué `setSummaryText` con el origen. | Si `bigText` repite el mismo texto corto, al expandir no cambia nada y la mejora no se nota. |
| «Guarda el historial de avisos con DataStore para que no se pierda al cerrar la app. El Repository no puede usar Context.» | Un `AvisosRepositoryDataStore` que guarda la lista como JSON en una clave de Preferences DataStore y la lee al iniciar. | El DataStore se crea en `AvisosApp` con `preferencesDataStore` y el Repository lo recibe ya armado junto con un `CoroutineScope` de la app. Descarté leer al inicio con `runBlocking`. El id del aviso nuevo sigue al mayor guardado y solo se guardan los últimos 50. | La tabla de capas prohíbe Context en el Repository. `runBlocking` congelaría el hilo principal al abrir la app. Si el id volviera a empezar en 1 después de reabrir, una notificación nueva reemplazaría a otra con el mismo id. |
| «Revisa que ninguna mejora rompa las reglas: sin Context ni NotificationManager en el ViewModel y todo receptor se quita en onDispose.» | Un `grep` sobre `viewmodel/` y `data/` y la lista de `registerReceiver`. | Nada que cambiar: solo aparecen comentarios y el único receptor se quita en `onDispose`. | Es lo que se revisa en la sustentación. |

## Cómo lo probé

- **Dos canales:** con Recordar en 10 s, el aviso sale emergente arriba de la pantalla. En Ajustes → AvisosCampus → Notificaciones aparecen los canales Avisos y Urgentes. Mostrar aviso sigue usando Avisos.
- **Texto largo:** contraída la notificación muestra una línea; expandida muestra el texto completo con el origen y la hora.
- **Historial persistente:** generé tres avisos, cerré la app con `adb shell am force-stop` y al abrirla de nuevo el historial seguía igual.
