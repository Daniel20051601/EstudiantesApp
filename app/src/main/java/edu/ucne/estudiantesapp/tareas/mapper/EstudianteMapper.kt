package edu.ucne.estudiantesapp.tareas.mapper

import edu.ucne.estudiantesapp.data.remote.dto.EstudianteDto
import edu.ucne.estudiantesapp.domain.model.Estudiante

fun EstudianteDto.toEstudiante(): Estudiante = Estudiante(
    estudianteId = estudianteId,
    nombre = nombre,
    carrera = carrera,
    edad = edad
)

fun Estudiante.toEstudianteDto(): EstudianteDto = EstudianteDto(
    estudianteId = estudianteId,
    nombre = nombre,
    carrera = carrera,
    edad = edad
)