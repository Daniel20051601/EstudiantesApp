package edu.ucne.estudiantesapp.tareas.presentation.estudiantes.list

import edu.ucne.estudiantesapp.domain.model.Estudiante

data class ListEstudianteUiState(
    val estudiantes: List<Estudiante> = emptyList(),
    val isLoading: Boolean = false
)