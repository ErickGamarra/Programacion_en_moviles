# Prompts utilizados — Fase 2 (rama `mejora-ia-huellitasvet`)

Mejoras de HuellitasVet hechas con ayuda de IA sobre la Fase 1 terminada en `main`, sin romper la arquitectura MVVM + Repository.

- **Herramienta usada:** Claude Code (asistente de IA). La guía sugiere Gemini en Android Studio; los prompts de abajo son los mismos que se le darían a Gemini y se pueden reutilizar tal cual.
- **Rama:** `mejora-ia-huellitasvet`. El nombre `mejora-ia` ya estaba ocupado en el repositorio por la mejora del laboratorio de la semana 6 (Clínica SaludPlus) y no se sobrescribió.
- **Mejoras elegidas:** gráfico de peso (commit A), reprogramar cita (commit B) y carnet de vacunas (commit C). La "búsqueda de pacientes" no se eligió porque ya está hecha en la Fase 1 (Paso 12: buscador por mascota o dueño + chips de especie con `combine`).
- **Evidencia:** capturas en `capturas/mejora-ia/`, generadas con la app real por `CapturasMejorasTest` (Robolectric, sin emulador), y la prueba `MigracionVacunasTest` para la migración de la base.

Reglas que se repitieron en los tres prompts (condición obligatoria de la guía):

1. La View (`ui/`) solo conoce a su ViewModel: nada de DAO, Repository ni reglas de negocio en los Composables.
2. El ViewModel solo conoce la interface `VetRepository`.
3. Toda función nueva del Repository se declara primero en `VetRepository` y luego se implementa en `VetRepositoryRoom`.
4. Lecturas que actualizan la pantalla → `Flow` (sin `suspend`) convertido a `StateFlow` con `stateIn`; escrituras → `suspend`.
5. Si se agrega o cambia una entidad, se sube la `version` de `VetDatabase` y se escribe la migración.

---

## Prompt 1 — Gráfico de peso de la mascota (commit A)

**Texto enviado a la IA:**

> Tengo una app Android llamada HuellitasVet en Kotlin con Jetpack Compose y Material 3 (sin XML), arquitectura MVVM + Repository, Room 2.8 y java.time (minSdk 26). El paquete base es `com.gamarra.huellitasvet`. La tabla `citas` (entidad `Cita`) guarda `mascotaId`, `fecha` (String "aaaa-MM-dd"), `hora` (String "HH:mm"), `estado` (String: PENDIENTE | CONFIRMADA | ATENDIDA | CANCELADA) y `pesoRegistrado` (Double?), que se llena al registrar una atención.
>
> Quiero que la ficha de la mascota (`ui/dueno/FichaMascotaScreen.kt`, pestaña "Historial clínico") muestre arriba del historial un gráfico de línea con la evolución del peso en cada cita ATENDIDA.
>
> Requisitos por capa:
> 1. **Modelo:** crea `model/PesoRegistrado.kt` con `data class PesoRegistrado(val fecha: String, val peso: Double)`.
> 2. **DAO:** en `CitaDao` agrega `fun observarPesos(mascotaId: Int): Flow<List<PesoRegistrado>>` con una consulta que devuelva `fecha` y `pesoRegistrado AS peso` solo de citas ATENDIDAS de esa mascota con `pesoRegistrado` no nulo, ordenadas por fecha y hora ascendente (la más antigua primero, porque el eje X es el tiempo).
> 3. **Repository:** declara `fun observarPesos(mascotaId: Int): Flow<List<PesoRegistrado>>` primero en la interface `VetRepository` y después implementarla en `VetRepositoryRoom` delegando en el DAO. Sin `suspend`.
> 4. **ViewModel:** en `MascotaViewModel` agrega `fun pesos(mascotaId: Int): StateFlow<List<PesoRegistrado>>` con `stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())`, igual que la función `historial` que ya existe.
> 5. **Componente:** crea `ui/components/graficos/GraficoLineas.kt` dibujado solo con `Canvas` (sin librerías externas). Debe recibir `List<DatoGrafico>` (ya existe: `data class DatoGrafico(val etiqueta: String, val valor: Float, val color: Color)`), escalar los valores entre el mínimo y el máximo, dibujar la línea, un área con degradado debajo, un punto por dato, el valor encima de cada punto y la fecha debajo (usa `rememberTextMeasurer` y `drawText`). Si hay menos de 2 datos, muestra el texto "Se necesitan al menos 2 atenciones con peso".
> 6. **View:** en `FichaMascotaScreen` observa `vm.pesos(mascotaId)` con `remember(mascotaId)` + `collectAsState()` y muestra una tarjeta "Evolución del peso (kg)" con `GraficoLineas`. La etiqueta de cada punto es la fecha corta ("23 jun."); agrega `fechaDiaMes(fecha)` en `ui/components/Formatos.kt` con `DateTimeFormatter.ofPattern("d MMM", Locale "es-PE")`. Usa `MaterialTheme.colorScheme.primary`, no colores fijos.
>
> Restricciones: no uses XML ni librerías externas, no cambies la versión de la base (no hay entidad nueva), nombres en español como el resto del proyecto, comentarios mínimos. Muéstrame solo los archivos que cambian.

**Qué generó la IA:**

- `model/PesoRegistrado.kt` (nuevo).
- `CitaDao.observarPesos` → `VetRepository` (interface) → `VetRepositoryRoom`.
- `MascotaViewModel.pesos(mascotaId)` como `StateFlow`.
- `ui/components/graficos/GraficoLineas.kt` (nuevo, Canvas con línea, área con degradado, puntos, valores y fechas).
- `FichaMascotaScreen`: tarjeta "Evolución del peso (kg)" en la pestaña Historial clínico; `Formatos.fechaDiaMes`.
- Captura `capturas/mejora-ia/A_grafico_peso.png`: Luna con 4.0 → 4.2 → 4.3 kg.

**Qué tuve que corregir yo:**

- Nada en el código de la app. En la prueba de capturas hubo que esperar a que Room cargue a Luna antes de tocarla (la primera versión de la prueba la buscaba demasiado pronto).
- En el prompt hubo que especificar el orden ascendente de la consulta: el historial clínico se ordena del más reciente al más antiguo, pero el gráfico necesita el orden inverso.

**¿Respeta MVVM?:** Sí. La consulta está en el DAO, la función se declaró primero en la interface `VetRepository`, el ViewModel expone un `StateFlow` y la View solo convierte los datos a `DatoGrafico` y dibuja. No se tocó la versión de la base porque no hay entidad nueva.

---

## Prompt 2 — Reprogramar cita (commit B)

**Texto enviado a la IA:**

> En la misma app HuellitasVet (Kotlin, Compose, Material 3, MVVM + Repository, Room, java.time) quiero que el **dueño** pueda reprogramar una cita desde "Mis citas" (`ui/dueno/MisCitasScreen.kt`), cambiando su fecha y hora.
>
> Reglas de negocio (van en el ViewModel, no en la View ni en el DAO):
> - Solo se reprograma una cita PENDIENTE o CONFIRMADA y solo si faltan **más de 2 horas** para la cita actual (misma regla que cancelar: `Duration.between(ahora, fechaHoraCita).toMinutes() > 120`).
> - La nueva fecha va de hoy a 7 días y la hora es uno de los 18 horarios de `model/Horarios.delDia` (09:00 a 17:30 cada 30 min).
> - Un horario ocupado (cita no cancelada en esa fecha y hora, usa `repo.observarHorasOcupadas(fecha)`) o que ya pasó aparece gris y tachado con el componente `HoraChip` existente y no se puede elegir.
> - No se puede elegir el mismo horario que ya tiene la cita.
> - Antes de guardar, vuelve a consultar las horas ocupadas (`observarHorasOcupadas(fecha).first()`); si alguien tomó ese horario mientras tanto, muestra "Ese horario ya no está disponible, elige otro" y no guarda.
> - Al reprogramar, la cita vuelve a estado PENDIENTE para que la clínica la confirme otra vez.
>
> Requisitos por capa:
> 1. **DAO:** en `CitaDao` agrega `suspend fun reprogramar(id: Int, fecha: String, hora: String)` con un `UPDATE` que cambie fecha, hora y deje `estado = 'PENDIENTE'`.
> 2. **Repository:** declara `suspend fun reprogramarCita(id: Int, fecha: String, hora: String)` primero en `VetRepository` y luego en `VetRepositoryRoom`.
> 3. **ViewModel nuevo:** `viewmodel/ReprogramarCitaViewModel.kt` con `cita: StateFlow<CitaConDetalle?>` (con `flatMapLatest` sobre `observarCita`), `fecha`, `hora`, `mensaje` como `StateFlow`, `horasOcupadas` con `flatMapLatest` sobre la fecha, `fechas` (hoy a 7 días), `horarios`, y las funciones `cargar(citaId)` (deja elegida la fecha actual de la cita), `elegirFecha` (limpia la hora), `elegirHora`, `puedeReprogramar(cita)`, `estaDisponible(hora, ocupadas)` y `confirmar(onReprogramada)`. Regístralo en `VetViewModelFactory`.
> 4. **View nueva:** `ui/dueno/ReprogramarCitaScreen.kt` con `TopAppBar` "Reprogramar cita", la cita actual con el componente `CitaCard`, un aviso "Puedes reprogramar hasta 2 horas antes. La cita vuelve a quedar PENDIENTE hasta que la clínica la confirme.", chips de fecha con `FechaChip`, cuadrícula de horas con `HoraChip` en un `FlowRow`, el texto "Nuevo horario: …" cuando hay hora elegida y el botón "Confirmar cambio" (componente `VetButton`).
> 5. **Mis citas:** en la pestaña Próximas, junto a "Cancelar", agrega el botón "Reprogramar" con la misma condición; cambia el aviso a "Puedes cancelar o reprogramar hasta 2 horas antes". La pantalla recibe `onReprogramar: (Int) -> Unit`.
> 6. **Navegación:** ruta `reprogramar/{citaId}` en `Rutas` y su `composable` en `NavVet`; al confirmar vuelve a Mis citas (`popBackStack`).
>
> Restricciones: sin XML ni librerías externas, la View no llama al Repository, nombres en español, comentarios mínimos. Muéstrame solo los archivos que cambian.

**Qué generó la IA:**

- `CitaDao.reprogramar` → `VetRepository.reprogramarCita` (interface) → `VetRepositoryRoom`.
- `viewmodel/ReprogramarCitaViewModel.kt` (nuevo) con todas las reglas.
- `ui/dueno/ReprogramarCitaScreen.kt` (nuevo).
- `MisCitasScreen` con el botón "Reprogramar"; ruta en `Rutas` y `NavVet`; registro en `VetViewModelFactory`.
- Capturas `B1` (Mis citas con "Reprogramar"), `B2` (nueva fecha y hora elegidas) y `B3` (la cita de Max pasó del 23 oct. 16:00 al 24 oct. 11:00 y quedó PENDIENTE).

**Qué tuve que corregir yo:**

- No hubo que corregir código después de generarlo: compiló y el flujo completo funcionó en la primera prueba (captura `B3`).
- Lo que sí hubo que decidir yo y escribir en el prompt, porque la guía no lo dice: que la cita reprogramada vuelve a PENDIENTE (si no, una cita CONFIRMADA quedaría confirmada en un horario que la clínica no aprobó) y que no se puede "reprogramar" al mismo horario.

**¿Respeta MVVM?:** Sí. Las reglas (2 horas, estados, horarios libres, rango de fechas, doble verificación) están en `ReprogramarCitaViewModel`; el ViewModel solo usa `VetRepository`; la función nueva se declaró primero en la interface; la View solo observa y avisa eventos.

---

## Prompt 3 — Carnet de vacunas (commit C)

**Texto enviado a la IA:**

> En HuellitasVet (Kotlin, Compose, Material 3, MVVM + Repository, Room 2.8, java.time) quiero un **carnet de vacunas** por mascota, visible en la ficha (`FichaMascotaScreen`) como una tercera pestaña "Vacunas" junto a "Datos" e "Historial clínico".
>
> Datos: nueva entidad `Vacuna` en `data/local/entity/Vacuna.kt` con `@Entity(tableName = "vacunas")`, `id` (Int, PK autogenerada), `mascotaId` (Int, `ForeignKey` hacia `Mascota` con `onDelete = CASCADE` para que eliminar una mascota no falle por sus vacunas), `nombre` (String), `fecha` (String "aaaa-MM-dd", fecha aplicada) y `proximaDosis` (String?, opcional). Índice en `mascotaId`.
>
> Requisitos por capa:
> 1. **DAO nuevo:** `VacunaDao` con `insertar`, `insertarTodos`, `eliminar` (suspend) y `observarPorMascota(mascotaId): Flow<List<Vacuna>>` ordenado por fecha descendente.
> 2. **Base de datos:** agrega `Vacuna::class` a `@Database`, **sube la versión de 1 a 2**, agrega `abstract fun vacunaDao()` y escribe `MIGRACION_1_2` que cree la tabla `vacunas` y su índice con el mismo SQL que genera Room (que un usuario con la versión 1 instalada no pierda datos). Regístrala con `addMigrations`.
> 3. **Datos iniciales:** en `DatosIniciales.sembrar` agrega vacunas de ejemplo (Luna: Triple felina al día y Antirrábica con refuerzo en menos de 30 días; Max: una vacuna con la próxima dosis vencida) usando fechas relativas con `LocalDate.now().plusDays(n)`.
> 4. **Repository:** declara primero en `VetRepository` y luego implementa en `VetRepositoryRoom`: `observarVacunas(mascotaId): Flow<List<Vacuna>>`, `suspend guardarVacuna(v)` y `suspend eliminarVacuna(v)`.
> 5. **ViewModel nuevo:** `VacunasViewModel` con `vacunas(mascotaId): StateFlow<List<Vacuna>>`, `errores: StateFlow<ErroresVacuna>` (un error por campo), `guardar(mascotaId, nombre, fecha, proximaDosis, onGuardada)`, `eliminar(v)`, `limpiar()` y `estado(v): EstadoVacuna`. Validaciones: nombre de al menos 3 caracteres; fecha aplicada con formato válido y no futura; próxima dosis opcional, con formato válido y posterior a la fecha aplicada. Estado (enum `model/EstadoVacuna`): VENCIDA si la próxima dosis ya pasó, PROXIMA si vence en 30 días o menos, AL_DIA si falta más, DOSIS_UNICA si no tiene próxima dosis. Regístralo en `VetViewModelFactory`.
> 6. **View:** `ui/dueno/CarnetVacunas.kt` con la lista de vacunas (ícono, nombre, fecha aplicada, próxima dosis, chip de estado con los colores del tema: verde al día, ámbar próxima, rojo vencida) y un botón "Registrar vacuna" que abre un formulario con `VetTextField` (Vacuna, Fecha aplicada aaaa-mm-dd con hoy por defecto, Próxima dosis opcional) y botones Cancelar/Guardar; los errores salen junto a cada campo. Agrega la pestaña "Vacunas" en `FichaMascotaScreen` (que recibe también el `VacunasViewModel`) y pásalo desde `NavVet` en la ficha del dueño y en la del veterinario.
>
> Restricciones: sin XML ni librerías externas, la View no conoce DAO ni Repository, nombres en español, comentarios mínimos. Muéstrame solo los archivos que cambian.

**Qué generó la IA:**

- `entity/Vacuna.kt`, `dao/VacunaDao.kt`, `model/EstadoVacuna.kt`, `viewmodel/VacunasViewModel.kt` y `ui/dueno/CarnetVacunas.kt` (nuevos).
- `VetDatabase` en versión 2 con `MIGRACION_1_2`; vacunas de ejemplo en `DatosIniciales`.
- `VetRepository` (interface) → `VetRepositoryRoom`; pestaña "Vacunas" en la ficha; `NavVet` y `VetViewModelFactory`.
- Prueba `MigracionVacunasTest`: crea una base versión 1 con Ana y Luna, la abre con la versión 2 y comprueba que Luna sigue y que se puede guardar una vacuna.
- Capturas `C1` (carnet de Luna), `C2` (errores "Mínimo 3 caracteres" y "No puede ser una fecha futura") y `C3` (Leucemia felina registrada).

**Qué tuve que corregir yo:**

- El formulario se generó primero dentro de un `AlertDialog`. La prueba automática de capturas no logró que la pantalla se estabilizara con ese diálogo abierto (otro diálogo sin campos sí funcionaba), así que se cambió por un formulario dentro de la misma pestaña. Además queda más cómodo: se ve el carnet mientras se escribe.
- Se comparó el SQL de la migración con el que genera Room en `VetDatabase_Impl` y se agregó la prueba de migración, porque un error ahí hace que la app se cierre al abrirla en un teléfono que ya tenía la versión 1.
- La decisión de usar `onDelete = CASCADE` en la llave foránea la tomé yo y la puse en el prompt: sin eso, eliminar una mascota sin citas pero con vacunas fallaría.

**¿Respeta MVVM?:** Sí. Entidad y DAO nuevos, versión de la base subida con su migración, funciones declaradas primero en la interface `VetRepository`, validaciones y cálculo del estado en `VacunasViewModel`, y la View solo muestra y avisa eventos.

---

## Reflexión

- **Qué mejoró la IA y qué no:** acelera mucho el trabajo repetitivo entre capas (DAO → interface → implementación → ViewModel → pantalla) y respeta bien los patrones que ya existen en el proyecto cuando el prompt los nombra. No decide sola las reglas que la guía deja abiertas (qué pasa con el estado al reprogramar, qué hacer con las vacunas al borrar una mascota); eso hay que pedirlo explícitamente.
- **Qué error cometió:** propuso un `AlertDialog` que la prueba automática no pudo validar, y la migración había que compararla con lo que genera Room; sin la prueba de migración, ese tipo de error solo aparece en un teléfono que ya tenía la app instalada.
- **Qué aprendí:** un buen prompt describe cada capa por separado y repite las reglas de MVVM. Las reglas de negocio viven en el ViewModel. Cambiar una entidad obliga a subir la versión de Room y a escribir la migración. Y lo que genera la IA hay que comprobarlo compilando y probando la app, no solo leyéndolo.
