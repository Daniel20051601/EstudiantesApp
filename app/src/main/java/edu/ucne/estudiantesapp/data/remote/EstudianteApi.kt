package edu.ucne.estudiantesapp.data.remote

import edu.ucne.estudiantesapp.data.remote.dto.EstudianteDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface EstudiantesApi {
    @GET("api/Estudiantes")
    suspend fun getEstudiantes(): List<EstudianteDto>

    @GET("api/Estudiantes/{id}")
    suspend fun getEstudiante(@Path("id") id: Int): EstudianteDto

    @POST("api/Estudiantes")
    suspend fun postEstudiante(@Body estudiante: EstudianteDto): Response<EstudianteDto>

    @PUT("api/Estudiantes/{id}")
    suspend fun putEstudiante(@Path("id") id: Int, @Body estudiante: EstudianteDto): Response<Unit>

    @DELETE("api/Estudiantes/{id}")
    suspend fun deleteEstudiante(@Path("id") id: Int): Response<Unit>
}