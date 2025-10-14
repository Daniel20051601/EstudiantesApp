package edu.ucne.estudiantesapp.domain.validation

data class ValidationResult(
    val isValid: Boolean,
    val errorMessage: String? = null
)
