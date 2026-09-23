# Portal Académico Institucional — Lab 05

## Datos Institucionales

* **Estudiante:** Erick Gamarra
* **Institución:** Tecsup
* **Curso:** Programación en Móviles
* **Laboratorio:** Semana 05 — Navegación en Jetpack Compose

---

## Registro de Avances y Fases

### Fase 1: Arquitectura Base y Flujos Funcionales
* Implementación de navegación estructurada con `NavHost` y `NavController`.
* Definición de rutas fuertemente tipadas mediante `sealed class Screen`.
* Paso de argumentos numéricos seguros (`NavType.IntType`) hacia la pantalla de detalle.
* Control de retorno y prevención de instancias duplicadas mediante `popUpTo(Screen.Home.route) { inclusive = true }`.
* Punto de control congelado en la etiqueta Git: `base-funcional`.

### Fase 2: Rediseño Visual Institucional Asistido por IA
* Integración del nuevo punto de entrada de autenticación (`LoginScreen`) como `startDestination`.
* Reestructuración del Back Stack:
    * De `Login` a `Home`: Purga de la pantalla de inicio de sesión (`popUpTo(Screen.Login.route) { inclusive = true }`).
    * En `Home` y `Profile`: Cierre de sesión seguro con vaciado total del historial (`popUpTo(0) { inclusive = true }`).
* Implementación de la paleta institucional Material 3 (tonos violetas `#381E72` y `#5E439B`, fondos de tarjeta en gris suave `#F1F1F4` y acentos de alerta `#B3261E`).
* Modelado tipado de datos con la colección oficial `Student` (5 registros institucionales encabezados por Erick Gamarra).
* Cabeceras con degradados continuos rectangulares de extremo a extremo y avatares con borde destacado en Expediente y Perfil.

---

## Mapa de Pantallas

1. **`LoginScreen`:** Tarjeta central flotante blanca sin elementos sobrantes, campos institucionales con alternancia de visibilidad y botón primario violeta.
2. **`HomeScreen`:** Menú principal con fondo en degradado vertical violeta, saludo centralizado, tarjetas de navegación con elevación suave y botón inferior de cierre de sesión plano sin contorno.
3. **`ListScreen`:** Directorio estructurado en `LazyColumn` sobre tarjetas en gris claro (`#F1F1F4`), con avatares inicializados y títulos de carrera en violeta.
4. **`DetailScreen`:** Expediente académico con cabecera rectangular degradada de extremo a extremo, avatar superpuesto con aro blanco, tarjetas de metadatos con iconos en gris suave y biografía.
5. **`ProfileScreen`:** Configuración de perfil con banner rectangular violeta continuo, agrupación modular (*INFORMACIÓN PERSONAL* y *ACADÉMICO*) con iconos en fondo gris y botón de salida en tono rojizo pálido.

---

## ️Stack Tecnológico

* **Lenguaje:** Kotlin
* **UI Toolkit:** Jetpack Compose & Material 3
* **Navegación:** `androidx.navigation:navigation-compose:2.7.7`
* **Iconografía:** `androidx.compose.material:material-icons-extended`
* **Control de versiones:** Git con ramas por características y tags semánticos