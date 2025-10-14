package edu.ucne.estudiantesapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class EstudianteDto(
    @SerializedName("estudianteId")
    val estudianteId: Int,

    @SerializedName("nombre")
    val nombre: String,

    @SerializedName("carrera")
    val carrera: String,

    @SerializedName("edad")
    val edad: Int
)
