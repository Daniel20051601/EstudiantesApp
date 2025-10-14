package edu.ucne.estudiantesapp.tareas.presentation.estudiantes.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import edu.ucne.estudiantesapp.domain.model.Estudiante


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListEstudianteScreen(
    navController: NavController,
    viewModel: ListEstudianteViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    val currentOnResume by rememberUpdatedState(viewModel::loadEstudiantes)
    DisposableEffect(navController) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                currentOnResume()
            }
        }
        val lifecycle = navController.currentBackStackEntry?.lifecycle
        lifecycle?.addObserver(observer)
        onDispose {
            lifecycle?.removeObserver(observer)
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {Text("Estudiantes")},
                windowInsets = WindowInsets(0.dp)
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate("edit_estudiante_screen/0")
            }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Agregar Estudiante"
                )
            }
        }

    ) {innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ){
            if(state.isLoading){
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }else if(state.estudiantes.isEmpty()){
                Text(
                    text = "No hay estudiantes agregados aún, agrega uno usando el botón +",
                    modifier = Modifier.align(Alignment.Center)
                )
            }else{
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ){
                    items(
                        state.estudiantes,
                        key = {estudiante -> estudiante.estudianteId}
                    ){
                        estudiante -> ItemEstudiante(
                            estudiante = estudiante,
                            onClick = {
                                navController.navigate("edit_estudiante_screen/${estudiante.estudianteId}")
                            }
                        )
                    }
                }

            }

        }


    }

}

@Composable
fun ItemEstudiante(
    estudiante: Estudiante,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
){
    Card(
        modifier = modifier
            .padding(4.dp)
            .fillMaxSize()
            .clickable { onClick() }
    ) {
        Row (
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxSize()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 4.dp)
        ) {
            Text(
                text= estudiante.nombre,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.weight(1f ))
            Text(
                text = estudiante.carrera,
                fontSize = 14.sp
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxSize()
                .padding(bottom = 16.dp, start = 16.dp)
        ) {
            Text(
                text = "Edad: ${estudiante.edad}",
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 4.dp, start = 4.dp)
            )
        }

    }
}

@Composable
@Preview
fun PreviewItemEstudiante(){
    ItemEstudiante(
        estudiante = Estudiante(
            estudianteId = 1,
            nombre = "Juan Perez",
            edad = 21,
            carrera = "Ingeniería en Sistemas"
        ),
        onClick = {}
    )
}
