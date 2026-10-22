package com.gamarra.huellitasvet.data.repository

import com.gamarra.huellitasvet.data.local.entity.Mascota
import com.gamarra.huellitasvet.data.local.entity.Servicio
import com.gamarra.huellitasvet.data.local.entity.Usuario
import com.gamarra.huellitasvet.data.local.entity.Vacuna
import com.gamarra.huellitasvet.data.local.relation.CitaConDetalle
import com.gamarra.huellitasvet.data.local.relation.MascotaConDueno
import com.gamarra.huellitasvet.model.ConteoServicio
import com.gamarra.huellitasvet.model.EstadoCita
import com.gamarra.huellitasvet.model.PesoRegistrado
import kotlinx.coroutines.flow.Flow

interface VetRepository {
    suspend fun login(correo: String, clave: String): Usuario?
    suspend fun obtenerUsuario(id: Int): Usuario?

    fun observarServicios(): Flow<List<Servicio>>
    fun observarServiciosActivos(): Flow<List<Servicio>>
    suspend fun guardarServicio(s: Servicio)
    suspend fun eliminarServicio(s: Servicio)
    suspend fun cambiarActivo(id: Int, activo: Boolean)
    suspend fun servicioTieneCitas(id: Int): Boolean

    fun observarMascotasDeDueno(duenoId: Int): Flow<List<Mascota>>
    fun observarMascota(id: Int): Flow<Mascota?>
    fun observarTodasLasMascotas(): Flow<List<MascotaConDueno>>
    suspend fun guardarMascota(m: Mascota)
    suspend fun eliminarMascota(m: Mascota)
    suspend fun mascotaTieneCitas(id: Int): Boolean

    fun observarVacunas(mascotaId: Int): Flow<List<Vacuna>>
    suspend fun guardarVacuna(v: Vacuna)
    suspend fun eliminarVacuna(v: Vacuna)

    fun observarHorasOcupadas(fecha: String): Flow<List<String>>
    suspend fun crearCita(mascotaId: Int, servicioId: Int, fecha: String, hora: String, nota: String): Long
    fun observarCitasDeDueno(duenoId: Int): Flow<List<CitaConDetalle>>
    fun observarCitasPorFecha(fecha: String): Flow<List<CitaConDetalle>>
    fun observarCita(id: Int): Flow<CitaConDetalle?>
    fun observarHistorial(mascotaId: Int): Flow<List<CitaConDetalle>>
    fun observarPesos(mascotaId: Int): Flow<List<PesoRegistrado>>
    suspend fun cambiarEstadoCita(id: Int, estado: EstadoCita)
    suspend fun reprogramarCita(id: Int, fecha: String, hora: String)
    suspend fun registrarAtencion(citaId: Int, diagnostico: String, tratamiento: String, peso: Double)

    fun observarCitasDeFecha(fecha: String): Flow<Int>
    fun observarCantidadPorEstado(estado: EstadoCita): Flow<Int>
    fun observarCitasPorServicio(): Flow<List<ConteoServicio>>
    fun observarIngresosDesde(fechaInicio: String): Flow<Double>
}
