package edu.ucne.estudiantesapp.domain.usecase

import edu.ucne.estudiantesapp.data.remote.repository.EstudianteRepository
import edu.ucne.estudiantesapp.domain.model.Estudiante
import edu.ucne.estudiantesapp.domain.validation.EstudianteValidator
import javax.inject.Inject

class PostEstudianteUseCase @Inject constructor(
    private val repository: EstudianteRepository,
    private val validator: EstudianteValidator
) {
    suspend operator fun invoke(estudiante: Estudiante): Result<Estudiante> {
        val nombreValidation = validator.validateNombre(estudiante.nombre)
        if(!nombreValidation.isValid){
            return Result.failure(IllegalArgumentException(nombreValidation.errorMessage))
        }

        val carreraValidation = validator.validateCarrera(estudiante.carrera)
        if(!carreraValidation.isValid){
            return Result.failure(IllegalArgumentException(carreraValidation.errorMessage))
        }

        val edadStr = estudiante.edad.toString()
        val edadValidation = validator.validateEdad(edadStr)
        if(!edadValidation.isValid){
            return Result.failure(IllegalArgumentException(edadValidation.errorMessage))
        }

        return runCatching { repository.postEstudiante(estudiante) }
    }
}