package edu.ucne.aluminiossamgui.data.mapper

import edu.ucne.aluminiossamgui.data.local.entity.TrabajoEntity
import edu.ucne.aluminiossamgui.domain.model.Trabajo

fun TrabajoEntity.toDomain() = Trabajo(
    trabajoId = trabajoId,
    nombre = nombre,
    nombreCliente = nombreCliente,
    telefonoCliente = telefonoCliente,
    direccion = direccion,
    notas = notas
)

fun Trabajo.toEntity() = TrabajoEntity(
    trabajoId = trabajoId,
    nombre = nombre,
    nombreCliente = nombreCliente,
    telefonoCliente = telefonoCliente,
    direccion = direccion,
    notas = notas
)

