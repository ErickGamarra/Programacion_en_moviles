# PROMPTS.md · Fase 2 (rama mejora-ia-murocampus)

Usé Gemini en Android Studio (modo Agent). Gemini hacía los cambios en el código y yo los revisaba con `git diff`, los probaba en el emulador y corregía lo que estaba mal antes de hacer cada commit.

| Prompt que escribí | Qué respondió Gemini | Qué cambié o descarté | Por qué |
|---|---|---|---|
| «En MuroCampus (MVVM + Repository con Retrofit) agrega la opción de borrar una publicación desde la pantalla de detalle. Usa @DELETE("posts/{id}") en PostApi devolviendo Response<Unit>, agrega borrar(id): Result<Unit> en PostRepository y PostRepositoryRemote revisando el código de respuesta, una función borrar en PostsViewModel con su propio StateFlow de UiState y un botón Eliminar en PantallaDetalle que muestre el resultado. No uses Retrofit ni HttpException en el ViewModel y no llames a la API desde un Composable.» | Agregó `@DELETE` en PostApi, la función `borrar()` en el Repository y en el ViewModel, y el botón «Eliminar publicación» con su mensaje. | Después de borrar, Gemini volvía a descargar toda la lista. Lo cambié para que solo se quite la publicación de la lista de la app. | JSONPlaceholder no borra de verdad, así que al recargar la publicación volvía a aparecer. Además era una descarga innecesaria. |
| (Gemini pidió permiso para leer archivos fuera del proyecto) | — | Le dije que no. | El repositorio tiene otros laboratorios y no hacía falta. Los cambios los revisé yo. |
| «Ahora agrega un campo de búsqueda en PantallaLista, encima de los chips de usuario, que filtre por título las publicaciones que ya están descargadas, sin hacer otra llamada a la API. La lógica del filtro debe ir en PostsViewModel con un StateFlow para el texto de búsqueda, y la pantalla solo debe dibujar el resultado. Si no hay coincidencias muestra Sin publicaciones. No cambies el Repository ni la API.» | Agregó el campo «Buscar por título» y la función `buscar()` en el ViewModel, que filtra lo que ya se descargó. | Hice que solo busque cuando la lista ya cargó. | Si escribías mientras salía el error de internet, el error desaparecía junto con el botón Reintentar. |
| «Agrega en RetrofitClient un Interceptor de OkHttp que añada el encabezado Accept: application/json a todas las peticiones. Debe ir antes del HttpLoggingInterceptor para que el encabezado se vea en Logcat. Solo modifica RetrofitClient; no toques el ViewModel, el Repository ni las pantallas, y mantén la baseUrl con https.» | Agregó un interceptor que pone `Accept: application/json` en cada petición. | Cambié `addHeader` por `header`. | Con `addHeader` el encabezado podía salir repetido; con `header` siempre hay uno solo. |

## Revisión de reglas

- Gemini no dejó ningún catch vacío: los errores siguen pasando por el Repository.
- El ViewModel no usa Retrofit, PostApi ni HttpException, y las pantallas no llaman a la API.
- No se usó `GlobalScope` y la URL sigue con `https://`.
