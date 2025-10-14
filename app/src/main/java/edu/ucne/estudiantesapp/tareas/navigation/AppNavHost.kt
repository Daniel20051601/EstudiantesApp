package edu.ucne.estudiantesapp.tareas.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import edu.ucne.estudiantesapp.tareas.presentation.estudiantes.edit.EditEstudianteScreen
import edu.ucne.estudiantesapp.tareas.presentation.estudiantes.list.ListEstudianteScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
){
    NavHost(
        navController = navController,
        startDestination = Screen.ListStudiante.route,
        modifier = modifier
    ){
        composable(Screen.ListStudiante.route){
            ListEstudianteScreen(navController = navController)
        }

        composable(
            route = Screen.EditEstudiante.route,
            arguments = listOf(
                navArgument(Screen.EditEstudiante.ARG){
                    type = NavType.IntType
                }
            )
        ){
            backStackEntry ->
            val estudianteId = backStackEntry.arguments?.getInt(Screen.EditEstudiante.ARG)
            EditEstudianteScreen(
                navController = navController,
                estudianteId = estudianteId
            )
        }
    }

}