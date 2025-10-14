package edu.ucne.estudiantesapp.data.remote

import edu.ucne.estudiantesapp.data.remote.dto.EstudianteDto
import javax.inject.Inject

class EstudianteRemoteDataSource @Inject constructor(
    private val api: EstudiantesApi
) {
    suspend fun getEstudiantes() = api.getEstudiantes()

    suspend fun getEstudiante(id: Int) = api.getEstudiante(id)

    suspend fun putEstudiante(id: Int, estudiante: EstudianteDto) = api.putEstudiante(id, estudiante)
    suspend fun postEstudiante(estudiante: EstudianteDto) = api.postEstudiante(estudiante)

    suspend fun deleteEstudiante(id: Int) = api.deleteEstudiante(id)

}