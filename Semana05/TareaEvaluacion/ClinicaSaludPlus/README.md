# Registro de Prompts y Mejoras con IA — Clínica Salud+

Este documento documenta la interacción con herramientas de Inteligencia Artificial para la implementación de mejoras funcionales, de experiencia de usuario (UX) y control de estados en la rama `mejora-ia`.

---

## Mejora 1: Diálogo Modal de Confirmación (`AlertDialog`)

### Prompt utilizado
> *"En la pantalla PantallaMisCitas.kt de Jetpack Compose, la acción de cancelar cita actualmente remueve el elemento de forma inmediata sin confirmación. Refactoriza el componente para integrar un AlertDialog de Material 3 que solicite confirmación explícita al usuario antes de eliminar la cita, gestionando la visibilidad del modal mediante elevación de estado."*

### Análisis y Decisión Técnica
* **Problema identificado:** En interfaces táctiles, una acción destructiva directa sin confirmación genera cancelaciones accidentales.
* **Solución aplicada:** Se introdujo un estado `var citaParaCancelar by remember { mutableStateOf<Appointment?>(null) }`. La presencia de una cita activa el diálogo `AlertDialog`. Si el usuario confirma, se aplica el filtro sobre la lista; si cancela, el estado regresa a `null` sin mutar la colección.

---

## Mejora 2: Retroalimentación Visual Asíncrona (`SnackbarHost`)

### Prompt utilizado
> *"Agrega retroalimentación visual no invasiva en PantallaMisCitas.kt tras cancelar una cita. Al confirmar la eliminación en el AlertDialog, debe mostrarse un mensaje emergente tipo Snackbar indicando que la cita con dicho médico fue cancelada correctamente. Utiliza SnackbarHost, SnackbarHostState y corrutinas con rememberCoroutineScope."*

### Análisis y Decisión Técnica
* **Problema identificado:** Tras cerrar el modal, la desaparición repentina del elemento de la lista no ofrece confirmación explícita del resultado de la acción (vulnerando la visibilidad del estado del sistema según las heurísticas de Nielsen).
* **Solución aplicada:** Se encapsuló la pantalla en un contenedor `Box` con un `SnackbarHost` alineado en `BottomCenter`. Al confirmar la cancelación, se despacha una corrutina mediante `coroutineScope.launch { snackbarHostState.showSnackbar(...) }`.

---

## Historial de Commits en `mejora-ia`

1. `mejora(ia): agregar dialogo AlertDialog de confirmacion para cancelacion de citas`
2. `mejora(ia): integrar SnackbarHost para feedback visual al cancelar citas`
3. `docs(ia): agregar PROMPTS.md documentando interacciones y decisiones tecnicas`