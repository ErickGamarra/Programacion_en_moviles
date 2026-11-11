# PROMPTS.md — Fase 2 (rama `mejora-ia-pokemon`)

Registro de las consultas a la IA hechas en la Fase 2. La Fase 1 (rama `main`) termina en el commit `docs: README`.

## Consulta 1

- **Fecha:** 10/11/2026
- **Herramienta:** Claude (asistente de IA)
- **Prompt:** "Tengo un ListaViewModel que recibe un ItemRepository (interface) y un Notificador. Ayúdame a escribir pruebas unitarias con un repositorio falso: éxito, error sin internet, sin internet con datos guardados y que los favoritos no se pierdan al sincronizar."
- **Qué respondió (resumen):** Propuso un `RepositorioFalso` en memoria con un `MutableStateFlow`, un `NotificadorFalso` que guarda los avisos en una lista y usar `StandardTestDispatcher` con `Dispatchers.setMain` y `advanceUntilIdle()` de `kotlinx-coroutines-test`.
- **Qué usamos / qué cambiamos y por qué:** Se usó la idea del repositorio falso. Se agregaron dos casos más: error HTTP 500 (crear un `HttpException` con `Response.error`) y Reintentar después de un error. El repositorio falso cuenta las llamadas a `refrescar()` para comprobar que sí se volvió a pedir la lista.
- **Qué aprendimos:** Que el Repository sea una interface es lo que permite probar el ViewModel sin internet ni Room. Sin `setMain` el `viewModelScope` falla en las pruebas porque no existe el hilo principal de Android.

## Consulta 2

- **Fecha:** 10/11/2026
- **Herramienta:** Claude (asistente de IA)
- **Prompt:** "Quiero agregar un buscador por nombre a la lista y deslizar hacia abajo para sincronizar. ¿Dónde debe ir el filtro para no romper MVVM?"
- **Qué respondió (resumen):** Recomendó filtrar en el ViewModel (no en el composable), guardar el texto en un `StateFlow` y usar `PullToRefreshBox` de Material 3 con un estado `refrescando` separado del `UiState`, para que la lista no desaparezca mientras sincroniza.
- **Qué usamos / qué cambiamos y por qué:** Se aplicó tal cual, y se amplió el filtro para buscar también por número (`#004` o `4`). Se agregó una prueba unitaria de la búsqueda.
- **Qué aprendimos:** Si el filtro se hace en la pantalla, la lógica queda en el composable y no se puede probar. Un indicador de carga separado evita volver al estado Cargando y "parpadear" la lista.

## Consulta 3

- **Fecha:** 10/11/2026
- **Herramienta:** Claude (asistente de IA)
- **Prompt:** "Sin internet la lista funciona con Room, pero al tocar un Pokémon el detalle muestra error. ¿Cómo mejorarlo sin guardar todo el detalle en Room?"
- **Qué respondió (resumen):** Sugirió que `obtenerDetalle()` devuelva la ficha básica guardada (nombre, número e imagen) cuando ocurre una `IOException`, y que la pantalla muestre un aviso si `detalle` es `null`.
- **Qué usamos / qué cambiamos y por qué:** Se usó solo para `IOException`. Ante un error HTTP se sigue mostrando el error con Reintentar, porque ahí el problema no es la conexión.
- **Qué aprendimos:** Con poco cambio el modo sin conexión es más útil. La imagen también se ve porque Coil la guarda en caché.

## Reflexión

La IA ayudó sobre todo en las pruebas: explicó cómo controlar las corrutinas con `kotlinx-coroutines-test`, algo que no se vio en clase. Sus respuestas no se copiaron sin revisar: cada cambio se compiló, se probó en el emulador y se ajustó al proyecto (nombres de la guía, mensajes en español, casos extra). Una limitación es que la IA no conoce el estado real de la app; por ejemplo, no sabía que la lista ya filtraba las formas especiales (id ≥ 10000), así que hubo que darle ese contexto.
