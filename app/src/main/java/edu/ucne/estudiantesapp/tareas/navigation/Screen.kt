package edu.ucne.estudiantesapp.tareas.navigation

sealed class Screen(val route: String){
    data object ListStudiante: Screen("list_estudiante_screen")
    data object EditEstudiante: Screen("edit_estudiante_screen/{estudianteId}"){
        const val ARG = "estudianteId"
        fun createRoute(estudianteId: Int) = "edit_estudiante_screen/$estudianteId"
    }
}