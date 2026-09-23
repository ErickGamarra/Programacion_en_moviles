# Registro de Prompts y Mejoras con Asistencia de IA — Clínica Salud+

Este documento detalla los prompts ejecutados, el análisis técnico y las correcciones manuales realizadas durante la **Fase 2 (`mejora-ia`)** del proyecto integrador, cumpliendo con los criterios de evaluación de la rúbrica.

---

## Mejora 1: Diálogo Modal de Confirmación (`AlertDialog`)

### Prompt utilizado
> *"En la pantalla PantallaMisCitas.kt de Jetpack Compose, la acción de cancelar cita actualmente remueve el elemento de forma inmediata sin confirmación. Refactoriza el componente para integrar un AlertDialog de Material 3 que solicite confirmación explícita al usuario antes de eliminar la cita, gestionando la visibilidad del modal mediante elevación de estado."*

### Análisis y Decisión Técnica
* **Problema detectado:** En interfaces táctiles, una acción destructiva directa sin confirmación previa produce pérdidas accidentales de datos ante toques involuntarios.
* **Solución aplicada:** Se introdujo la variable de estado `var citaParaCancelar by remember { mutableStateOf<Appointment?>(null) }`. La existencia de una referencia activa condiciona el renderizado del `AlertDialog`. Si el usuario confirma, se aplica el filtro sobre la lista; si descarta, el estado regresa a `null` sin mutar los datos.

### Correcciones aplicadas sobre la respuesta de la IA
* La IA intentaba utilizar estilos y colores predeterminados de Material 2 (`MaterialTheme.colors`). Se corrigió hacia la API de Material 3 (`MaterialTheme.colorScheme.error` y `ButtonDefaults.textButtonColors`).

---

## Mejora 2: Retroalimentación Visual Asíncrona (`SnackbarHost`)

### Prompt utilizado
> *"Agrega retroalimentación visual no invasiva en PantallaMisCitas.kt tras cancelar una cita. Al confirmar la eliminación en el AlertDialog, debe mostrarse un mensaje emergente tipo Snackbar indicando que la cita con dicho médico fue cancelada correctamente. Utiliza SnackbarHost, SnackbarHostState y corrutinas con rememberCoroutineScope."*

### Análisis y Decisión Técnica
* **Problema detectado:** Tras el cierre del modal, la desaparición inmediata de la tarjeta no otorgaba retroalimentación confirmatoria de la operación, comprometiendo la visibilidad del estado del sistema (heurística de Nielsen).
* **Solución aplicada:** Se encapsuló la vista en un contenedor `Box` con un `SnackbarHost` alineado al pie (`Alignment.BottomCenter`). En la acción de confirmación, se despacha la llamada de suspensión mediante `coroutineScope.launch { snackbarHostState.showSnackbar(...) }`.

### Correcciones aplicadas sobre la respuesta de la IA
* **Conflicto de corrutinas en Composable:** La IA invocaba `showSnackbar` directamente en el callback del botón sin envolverlo en una corrutina. Se corrigió instanciando `rememberCoroutineScope()` para garantizar la ejecución asíncrona dentro del ciclo de vida del componente.
* **Conflicto binario de dependencias:** La IA sugirió actualizar a `navigation-compose:2.8.5`, generando incompatibilidad con el Compose BOM existente. Se mantuvo la versión estable `2.7.7`.

---

## Mejora 3: Fidelidad Visual UI/UX, Destinos del Drawer y Elevación de Estado (State Hoisting)

### Prompt utilizado
> *"Refactoriza la interfaz gráfica de Clínica Salud+ para que coincida exactamente con las figuras 1 y 2 de la rúbrica oficial (paleta institucional morada #4F236E, cabecera con 'Hola, Juan', avatares con contenedor de cruz morada, selectores de fecha/hora en bloques rectangulares y tarjetas de 'Mis citas' con franja lateral morada). Además, implementa las pantallas faltantes del menú lateral ('Mi perfil' e 'Historial médico') y aplica elevación de estado (State Hoisting) en MainScreen para que las citas creadas en el flujo de agendamiento se registren y visualicen de forma reactiva en 'Mis citas'."*

### Análisis y Decisión Técnica
* **Problemas detectados:**
    1. La interfaz utilizaba estilos predeterminados desalineados de las maquetas de la guía oficial.
    2. Los destinos «Perfil» e «Historial médico» en el menú lateral carecían de navegación funcional y pantallas destino.
    3. La colección de citas residía localmente en `PantallaMisCitas`, aislando las citas nuevas creadas en el flujo secuencial.
* **Soluciones aplicadas:**
    1. **Alineación de diseño:** Se introdujo la paleta institucional (`ClinicaMorado`, `ClinicaMoradoPastel`, `ClinicaMentaFondo`, `ClinicaGrisFondo`), selectores rectangulares con contraste activo y franja indicadora morada de 6 dp en las tarjetas.
    2. **Completitud del grafo de navegación:** Creación de `PantallaPerfilUsuario.kt` y `PantallaHistorialMedico.kt`, integradas al `NavHost` y al `ModalNavigationDrawer`.
    3. **State Hoisting:** Se elevó `listaCitas` a `MainScreen.kt`, pasando la lista y las funciones mutadoras por parámetro hacia `PantallaMisCitas` y actualizándola al confirmar en `PantallaAgendarCita`.

### Correcciones aplicadas sobre la respuesta de la IA
* **Incompatibilidad de modelo:** La IA instanció `Appointment(doctorId = ...)`, propiedad inexistente en la `data class`. Se eliminó dicho parámetro conservando la estructura de 6 atributos.
* **Falta de delegado de estado:** La asignación directa sobre `listaCitas` generaba error por falta del import `androidx.compose.runtime.setValue`. Se agregó el import manualmente.
* **Sintaxis de anidamiento en Compose:** Se eliminaron cierres de llaves desalineados generados dentro de `Scaffold { paddingValues -> ... }`.

---

## Historial de Commits en `mejora-ia`

1. `mejora(ia): agregar dialogo AlertDialog de confirmacion para cancelacion de citas`
2. `mejora(ia): integrar SnackbarHost para feedback visual al cancelar citas`
3. `refactor(ui): alinear diseno visual exacto segun guia y vincular pantallas de perfil e historial`
4. `docs(ia): documentar prompts, decisiones tecnicas y correcciones aplicadas`