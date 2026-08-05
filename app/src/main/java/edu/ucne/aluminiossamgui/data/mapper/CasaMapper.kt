package edu.ucne.aluminiossamgui.data.mapper

import edu.ucne.aluminiossamgui.data.local.entity.CasaEntity
import edu.ucne.aluminiossamgui.domain.model.Casa

fun CasaEntity.toDomain() = Casa(
    casaId = casaId,
    nombre = nombre,
    direccion = direccion
)

fun Casa.toEntity() = CasaEntity(
    casaId = casaId,
    nombre = nombre,
    direccion = direccion
)

