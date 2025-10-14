package edu.ucne.estudiantesapp.domain.usecase

import edu.ucne.estudiantesapp.data.remote.repository.EstudianteRepository
import javax.inject.Inject

class GetEstudiantesUseCase @Inject constructor(
    private val repository: EstudianteRepository
) {
    suspend operator fun invoke() = repository.getEstudiantes()
}