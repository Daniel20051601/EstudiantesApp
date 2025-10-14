package edu.ucne.estudiantesapp.tareas.presentation.estudiantes.edit

interface EditEstudianteUiEvent {
    data class nombreChanged(val nombre: String) : EditEstudianteUiEvent
    data class carreraChanged(val carrera: String) : EditEstudianteUiEvent
    data class edadChanged(val edad: String) : EditEstudianteUiEvent
    data class loadEstudiante(val id: Int?) : EditEstudianteUiEvent
    data object saveEstudiante : EditEstudianteUiEvent
    data object deleteEstudiante : EditEstudianteUiEvent

}