# Prompt Maestro para Gemini (Android Studio)

Actúa como un Desarrollador Android Senior especialista en Jetpack Compose, Arquitectura Limpia y Material 3.

Requiero que refactorices, sobreescribas y generes el código fuente íntegro de mi aplicación para implementar el "Portal Académico Institucional", calcando con exactitud quirúrgica las referencias visuales de la rúbrica institucional y cumpliendo estrictamente con la navegación oficial de Compose.

### INFORMACIÓN DEL ENTORNO
- Paquete base: com.gamarra.semana05_navegacion
- Dependencias disponibles: Navigation Compose 2.7.7, Material 3, androidx.compose.material:material-icons-extended
- Estudiante titular en sesión: Erick Gamarra (Tecsup)

---

### REGLAS DE ARQUITECTURA Y NAVEGACIÓN (ESTRICTAS)
1. Código 100% completo, sin omisiones ni comentarios "// TODO". Cada archivo debe incluir sus imports explícitos completos (sin asteriscos).
2. Contrato de rutas centralizado en sealed class Screen(val route: String) en el paquete navigation.
3. startDestination del NavHost = Screen.Login.route ("login").
4. Manejo estricto de la pila (Back Stack):
   - Login -> Home: popUpTo(Screen.Login.route) { inclusive = true }
   - Botón "Cerrar Sesión Segura" (Home) y botón "Cerrar Sesión" (Profile): deben navegar a Screen.Login.route limpiando toda la pila con popUpTo(0) { inclusive = true }
   - ListScreen, DetailScreen y ProfileScreen: botón de retroceso en TopAppBar ejecutando navController.popBackStack()
5. Paso de argumento a DetailScreen: tipado como NavType.IntType con la clave "itemId".
6. Añadir @OptIn(ExperimentalMaterial3Api::class) en todas las funciones con TopAppBar o Scaffold.

---

### PALETA DE COLORES EXACTA
- Violeta Oscuro Primario (Títulos y cabeceras): Color(0xFF381E72)
- Violeta Intermedio (Botón de login y texto de carreras): Color(0xFF5E439B)
- Lavanda / Fondo TopBar Lista: Color(0xFFEDE7F6)
- Contenedor Íconos Home: Color(0xFFEADDFF)
- Fondo General de Pantalla: Color(0xFFF3F1F8) o Color(0xFFF8F7FA)
- Fondo de Tarjetas de Información: Color(0xFFF1F1F4) (gris claro neutro)
- Fondo de Círculos de Íconos en Perfil y Detalle: Color(0xFFE0E0E0) (GRIS SUAVE, NO MORADO)
- Rojo Alerta / Logout: Color(0xFFB3261E)
- Contenedor Logout Perfil: Color(0xFFFCE8E6)

---

### ESPECIFICACIÓN DETALLADA POR ARCHIVO

#### 1. model/Student.kt
Paquete: com.gamarra.semana05_navegacion.model
- data class Student(val id: Int, val name: String, val career: String, val code: String, val email: String, val faculty: String, val bio: String)
- Lista estática studentList con 5 registros oficiales:
  * ID 1: "Erick Gamarra" | "Diseño y Desarrollo de Software" | "2024-0001" | "erick.gamarra@tecsup.edu.pe" | "Tecnología Digital" | "Estudiante destacado con interés en desarrollo móvil nativo en Android."
  * ID 2: "Maria Garcia" | "Arquitectura Digital" | "2024-0002" | "maria.garcia@tecsup.edu.pe" | "Diseño y Urbanismo" | "Especializada en modelado y diseño de interfaces funcionales."
  * ID 3: "Carlos Perez" | "Redes y Comunicaciones" | "2024-0003" | "carlos.perez@tecsup.edu.pe" | "Tecnología Digital" | "Enfocado en infraestructura de redes y seguridad de datos."
  * ID 4: "Ana Lopez" | "Mecatrónica Industrial" | "2024-0004" | "ana.lopez@tecsup.edu.pe" | "Ingeniería Aplicada" | "Proyectos de automatización y robótica colaborativa."
  * ID 5: "Luis Ramirez" | "Gestión de Datos" | "2024-0005" | "luis.ramirez@tecsup.edu.pe" | "Negocios Digitales" | "Analítica predictiva y optimización de flujos operativos."

#### 2. navigation/Screen.kt
Paquete: com.gamarra.semana05_navegacion.navigation
- sealed class Screen(val route: String):
  * object Login : Screen("login")
  * object Home : Screen("home")
  * object List : Screen("list")
  * object Profile : Screen("profile")
  * object Detail : Screen("detail/{itemId}") { fun createRoute(itemId: Int): String = "detail/$itemId" }

#### 3. screens/LoginScreen.kt
Paquete: com.gamarra.semana05_navegacion.screens
- Fondo neutro lavanda suave Color(0xFFF3F1F8).
- Card central blanca flotante con RoundedCornerShape(24.dp) y sombra suave (elevation 6.dp).
- Encabezado: Título "Portal Académico" en negrita Color(0xFF381E72), tamaño titleLarge, y subtítulo "Accede a tu cuenta" en gris Color(0xFF79747E).
- Dos OutlinedTextField con esquinas de 12.dp:
  * "Correo Institucional" con leadingIcon = Icons.Default.Email
  * "Contraseña" con leadingIcon = Icons.Default.Lock y trailingIcon de visibilidad (PasswordVisualTransformation / VisualTransformation.None con Icons.Default.Visibility / Icons.Default.VisibilityOff)
- Botón "INICIAR SESIÓN" sólido en Color(0xFF5E439B), esquinas de 12.dp y texto blanco bold. Navega a Home con popUpTo(Screen.Login.route) { inclusive = true }.
- Texto interactivo al pie: "¿Olvidaste tu contraseña?".

#### 4. screens/HomeScreen.kt
Paquete: com.gamarra.semana05_navegacion.screens
- Fondo vertical continuo con degradado: Brush.verticalGradient(listOf(Color(0xFF4A148C), Color(0xFF6750A4), Color(0xFFEDE7F6))).
- Disposición en Column(modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp, vertical = 20.dp)):
  * Spacer(Modifier.weight(1f)) para balance vertical superior.
  * Bloque Central:
    - "Bienvenido,\nErick Gamarra" en headlineMedium negrita blanco centrado.
    - "¿Qué deseas gestionar hoy?" en bodyLarge blanco translúcido centrado.
    - Espacio de 28.dp.
    - Card interactiva 1: "Directorio de Alumnos", subtítulo "Ver y gestionar estudiantes", leadingIcon Icons.Default.People en contenedor circular Color(0xFFEADDFF), chevron derecho. Navega a Screen.List.route.
    - Espacio de 16.dp.
    - Card interactiva 2: "Mi Perfil Académico", subtítulo "Datos personales y progreso", leadingIcon Icons.Default.Person en contenedor circular Color(0xFFEADDFF), chevron derecho. Navega a Screen.Profile.route.
  * Spacer(Modifier.weight(1f)) para empujar el botón al fondo.
  * Botón inferior de pie de página: TextButton o Row interactivo plano, SIN BORDE, SIN OUTLINE, con ícono Icons.Default.Logout y texto "Cerrar Sesión Segura" en color rojo Color(0xFFB3261E). Al pulsar, navega a Login limpiando toda la pila con popUpTo(0) { inclusive = true }.

#### 5. screens/ListScreen.kt
Paquete: com.gamarra.semana05_navegacion.screens
- Scaffold con fondo Color(0xFFF8F7FA).
- TopAppBar: fondo lavanda suave Color(0xFFEDE7F6), flecha atrás Color(0xFF381E72) y título "Directorio de Alumnos" en negrita morado oscuro Color(0xFF381E72).
- LazyColumn con espacio vertical de 10.dp y relleno horizontal de 16.dp iterando studentList.
- Tarjetas de alumnos con containerColor = Color(0xFFF1F1F4) (gris claro neutro), esquinas RoundedCornerShape(14.dp), elevación 0.dp:
  * Avatar circular izquierdo con inicial del estudiante sobre fondo violeta suave.
  * Columna central: Nombre en negrita oscura y carrera en texto morado intermedio Color(0xFF5E439B).
  * Ícono lateral Icons.Default.ChevronRight en gris.
  * Clickable que navega a Screen.Detail.createRoute(student.id).

#### 6. screens/DetailScreen.kt
Paquete: com.gamarra.semana05_navegacion.screens
- Parámetros: navController: NavController, itemId: Int. Recupera el alumno desde studentList.
- TopAppBar con flecha atrás y título "Expediente Académico".
- CABECERA CON RECTÁNGULO DEGRADADO DE EXTREMO A EXTREMO (fillMaxWidth()):
  * Contenedor superior con degradado vertical morado: Brush.verticalGradient(listOf(Color(0xFF381E72), Color(0xFF5E439B))) y esquinas inferiores redondeadas (24.dp).
  * Avatar circular grande centrado con aro blanco prominente (4.dp), superpuesto sobre la cabecera.
- Inmediatamente debajo del avatar:
  * Nombre del estudiante en headlineSmall negrita centrado.
  * Carrera en color morado Color(0xFF5E439B) centrado.
- Tarjeta de información institucional en fondo gris claro Color(0xFFF1F1F4), esquinas RoundedCornerShape(16.dp), conteniendo filas con:
  * Círculos o contenedores de íconos en COLOR GRIS SUAVE Color(0xFFE0E0E0) (NO MORADOS).
  * Filas para ID Estudiante ("2024-0001", Icons.Default.Badge), Correo Institucional ("erick.gamarra@tecsup.edu.pe", Icons.Default.Email) y Facultad ("Tecnología Digital", Icons.Default.School).
- Tarjeta de Biografía separada en fondo gris Color(0xFFF1F1F4) con título "Biografía" en negrita y el párrafo descriptivo del estudiante.

#### 7. screens/ProfileScreen.kt
Paquete: com.gamarra.semana05_navegacion.screens
- Scaffold con TopAppBar blanca neutra, flecha atrás y título morado "Configuración de Perfil".
- CABECERA EN RECTÁNGULO DEGRADADO DE EXTREMO A EXTREMO (fillMaxWidth):
  * Contenedor superior rectangular que cubre todo el ancho con degradado vertical violeta profundo a ciruela oscuro: Brush.verticalGradient(listOf(Color(0xFF381E72), Color(0xFF4F2768), Color(0xFF5D2F6B))) con esquinas inferiores redondeadas (24.dp).
  * Dentro del recuadro morado: Avatar circular amplio centrado con borde blanco, y justo debajo del avatar (dentro del mismo bloque), el nombre "Erick Gamarra" en tipografía blanca negrita.
- Contenido con padding horizontal (16.dp):
  * Título de categoría en mayúsculas "INFORMACIÓN PERSONAL".
  * Tarjeta en fondo gris Color(0xFFF1F1F4) con filas para Nombre Completo, Correo institucional y Teléfono. Cada ícono dentro de un contenedor circular o cuadrado redondeado en COLOR GRIS SUAVE Color(0xFFE0E0E0) (NO MORADOS).
  * Título de categoría en mayúsculas "ACADÉMICO".
  * Tarjeta en fondo gris Color(0xFFF1F1F4) con filas para Carrera, Institución y Ciclo Actual. Íconos igualmente en contenedores GRISES suaves.
  * Spacer(Modifier.weight(1f)) para empujar el botón al fondo.
  * Botón inferior "Cerrar Sesión": Contenedor tipo píldora en fondo rojo suave Color(0xFFFCE8E6) con texto e ícono Icons.Default.Logout en rojo carmesí Color(0xFFB3261E). Al pulsar, navega a Login limpiando toda la pila con popUpTo(0) { inclusive = true }.

#### 8. navigation/AppNavigation.kt
Paquete: com.gamarra.semana05_navegacion.navigation
- NavHost con startDestination = Screen.Login.route conectando las 5 pantallas, garantizando el paso del argumento itemId a DetailScreen y la limpieza de pila requerida.
```