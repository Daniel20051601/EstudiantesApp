package edu.ucne.estudiantesapp.domain.validation

import javax.inject.Inject

class EstudianteValidator @Inject constructor() {
    fun validateNombre(nombre: String): ValidationResult {
        return if (nombre.isBlank()) {
            ValidationResult(false, "El nombre no puede estar vacío")
        } else if (nombre.length < 3) {
            ValidationResult(false, "El nombre debe tener al menos 3 caracteres")
        }else if(nombre.length > 60){
            ValidationResult(false, "El nombre no puede tener más de 50 caracteres")
        } else {
            ValidationResult(true)
        }
    }

    fun validateCarrera(carrera: String): ValidationResult {
        return if (carrera.isBlank()) {
            ValidationResult(false, "La carrera no puede estar vacía")
        } else if (carrera.length < 3) {
            ValidationResult(false, "La carrera debe tener al menos 3 caracteres")
        }else if(carrera.length > 60){
            ValidationResult(false, "La carrera no puede tener más de 50 caracteres")
        } else {
            ValidationResult(true)
        }
    }

        fun validateEdad(edad: String): ValidationResult {
        val edadInt = edad.toIntOrNull()
        return if (edad.isBlank()) {
            ValidationResult(false, "La edad no puede estar vacía")
        } else if (edadInt == null || edadInt <= 0) {
            ValidationResult(false, "La edad debe ser un número positivo")
        } else if (edadInt > 100) {
            ValidationResult(false, "La edad no puede ser mayor a 100")
        }
        else {
            ValidationResult(true)
        }
    }
}