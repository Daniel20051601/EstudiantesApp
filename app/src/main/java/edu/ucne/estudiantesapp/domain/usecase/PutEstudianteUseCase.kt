package edu.ucne.estudiantesapp.domain.usecase

import edu.ucne.estudiantesapp.data.remote.repository.EstudianteRepository
import edu.ucne.estudiantesapp.domain.model.Estudiante
import edu.ucne.estudiantesapp.domain.validation.EstudianteValidator
import javax.inject.Inject

class PutEstudianteUseCase @Inject constructor(
    private val repository: EstudianteRepository,
    private val validator: EstudianteValidator
) {
    suspend operator fun invoke(id: Int, estudiante: Estudiante): Result<Boolean>{
        if(id <= 0){
            return Result.failure(IllegalArgumentException("ID inválido"))
        }

        val validationNombre = validator.validateNombre(estudiante.nombre)
        if(!validationNombre.isValid){
            return Result.failure(IllegalArgumentException(validationNombre.errorMessage))
        }

        val validationCarrera = validator.validateCarrera(estudiante.carrera)
        if(!validationCarrera.isValid){
            return Result.failure(IllegalArgumentException(validationCarrera.errorMessage))
        }

        val edadStr = estudiante.edad.toString()
        val validationEdad = validator.validateEdad(edadStr)
        if(!validationEdad.isValid){
            return Result.failure(IllegalArgumentException(validationEdad.errorMessage))
        }
        return runCatching { repository.putEstudiante(id, estudiante) }

    }
}