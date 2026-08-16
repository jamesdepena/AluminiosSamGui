package edu.ucne.aluminiossamgui.domain.usecase.hueco

import edu.ucne.aluminiossamgui.domain.model.ColorMaterial
import edu.ucne.aluminiossamgui.domain.model.Hueco
import edu.ucne.aluminiossamgui.domain.model.TipoHueco
import edu.ucne.aluminiossamgui.domain.model.TipoMaterial
import edu.ucne.aluminiossamgui.domain.usecase.ValidationResult
import jakarta.inject.Inject

class HuecoValidations @Inject constructor() {

    fun validateEtiqueta(etiqueta: String): ValidationResult {
        if (etiqueta.isBlank()) {
            return ValidationResult(
                isValid = false,
                errorMsg = "La etiqueta no puede estar vacía."
            )
        }

        return ValidationResult(isValid = true)
    }

    fun validateAnchoBase(anchoBase: Double): ValidationResult {
        if (anchoBase <= 0.0) {
            return ValidationResult(
                isValid = false,
                errorMsg = "El ancho debe ser mayor que 0."
            )
        }

        return ValidationResult(isValid = true)
    }

    fun validateLargoBase(largoBase: Double): ValidationResult {
        if (largoBase <= 0.0) {
            return ValidationResult(
                isValid = false,
                errorMsg = "El largo debe ser mayor que 0."
            )
        }

        return ValidationResult(isValid = true)
    }

    fun validateCorredera(hueco: Hueco): ValidationResult {
        val materialesPermitidos = listOf(
            TipoMaterial.TRADICIONAL,
            TipoMaterial.P65,
            TipoMaterial.P92
        )

        if (hueco.tipoMaterial !in materialesPermitidos) {
            return ValidationResult(
                isValid = false,
                errorMsg = "Debe seleccionar un material válido para la corredera."
            )
        }

        val coloresPermitidos = listOf(
            ColorMaterial.NEGRO,
            ColorMaterial.BLANCO,
            ColorMaterial.CAOBA,
            ColorMaterial.GRIS
        )

        if (hueco.color !in coloresPermitidos) {
            return ValidationResult(
                isValid = false,
                errorMsg = "El color seleccionado no está disponible para correderas."
            )
        }

        if (hueco.anchoPuerta != null || hueco.acabadoPuerta != null) {
            return ValidationResult(
                isValid = false,
                errorMsg = "Una corredera no puede tener atributos de puerta."
            )
        }

        return ValidationResult(isValid = true)
    }

    fun validatePuerta(hueco: Hueco): ValidationResult {
        if (hueco.tipoMaterial != null) {
            return ValidationResult(
                isValid = false,
                errorMsg = "Una puerta no utiliza tipo de material."
            )
        }

        if (hueco.esTresVias) {
            return ValidationResult(
                isValid = false,
                errorMsg = "Una puerta no puede configurarse como tres vías."
            )
        }

        if (hueco.anchoPuerta == null) {
            return ValidationResult(
                isValid = false,
                errorMsg = "Debe seleccionar el ancho de la puerta."
            )
        }

        if (hueco.acabadoPuerta == null) {
            return ValidationResult(
                isValid = false,
                errorMsg = "Debe seleccionar el acabado de la puerta."
            )
        }

        val coloresPermitidos = listOf(
            ColorMaterial.CAOBA,
            ColorMaterial.BLANCO,
            ColorMaterial.OTRO
        )

        if (hueco.color !in coloresPermitidos) {
            return ValidationResult(
                isValid = false,
                errorMsg = "El color seleccionado no está disponible para puertas."
            )
        }

        if (
            hueco.color == ColorMaterial.OTRO &&
            hueco.colorPersonalizado.isNullOrBlank()
        ) {
            return ValidationResult(
                isValid = false,
                errorMsg = "Debe escribir el color personalizado de la puerta."
            )
        }

        return ValidationResult(isValid = true)
    }

    fun validateCristalFijo(hueco: Hueco): ValidationResult {
        if (hueco.tipoMaterial != TipoMaterial.P40) {
            return ValidationResult(
                isValid = false,
                errorMsg = "El cristal fijo debe utilizar material P40."
            )
        }

        if (hueco.esTresVias) {
            return ValidationResult(
                isValid = false,
                errorMsg = "Un cristal fijo no puede configurarse como tres vías."
            )
        }

        val coloresPermitidos = listOf(
            ColorMaterial.NEGRO,
            ColorMaterial.BLANCO,
            ColorMaterial.GRIS,
            ColorMaterial.CAOBA
        )

        if (hueco.color !in coloresPermitidos) {
            return ValidationResult(
                isValid = false,
                errorMsg = "El color seleccionado no está disponible para cristal fijo."
            )
        }

        if (hueco.anchoPuerta != null || hueco.acabadoPuerta != null) {
            return ValidationResult(
                isValid = false,
                errorMsg = "Un cristal fijo no puede tener atributos de puerta."
            )
        }

        return ValidationResult(isValid = true)
    }

    fun validateHueco(hueco: Hueco): ValidationResult {
        val etiquetaResult = validateEtiqueta(hueco.etiqueta)
        if (!etiquetaResult.isValid) {
            return etiquetaResult
        }

        val anchoResult = validateAnchoBase(hueco.anchoBase)
        if (!anchoResult.isValid) {
            return anchoResult
        }

        val largoResult = validateLargoBase(hueco.largoBase)
        if (!largoResult.isValid) {
            return largoResult
        }

        return when (hueco.tipo) {
            TipoHueco.CORREDERA -> validateCorredera(hueco)
            TipoHueco.PUERTA -> validatePuerta(hueco)
            TipoHueco.CRISTAL_FIJO -> validateCristalFijo(hueco)
        }
    }
}