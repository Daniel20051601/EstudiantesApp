package edu.ucne.estudiantesapp.tareas.presentation.estudiantes.edit

data class EditEstudianteUiState(
    val estudianteId: Int? = 0,
    val nombre: String = "",
    val carrera: String = "",
    val edad: String = "",
    val nombreError: String? = null,
    val carreraError: String? = null,
    val edadError: String? = null,
    val isSaving: Boolean = false,
    val isSaved: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val canBeDeleted: Boolean = false,
    val isDeleting: Boolean = false,
)
