package com.gamarra.huellitasvet.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.gamarra.huellitasvet.data.local.entity.Mascota
import com.gamarra.huellitasvet.data.local.entity.Usuario

data class MascotaConDueno(
    @Embedded val mascota: Mascota,
    @Relation(parentColumn = "duenoId", entityColumn = "id")
    val dueno: Usuario
)
