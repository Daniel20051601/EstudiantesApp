package edu.ucne.estudiantesapp.tareas.presentation.estudiantes.edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import edu.ucne.estudiantesapp.domain.model.Estudiante

@Composable
fun EditEstudianteScreen(
    navController: NavController,
    viewModel: EditEstudianteViewModel = hiltViewModel(),
    estudianteId: Int?
) {
    LaunchedEffect(estudianteId) {
        viewModel.onEvent(EditEstudianteUiEvent.loadEstudiante(estudianteId))
    }

    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.isSaved) {
        if(state.isSaved){
            navController.popBackStack()
        }
    }

    EditEstudianteBody(
        state = state,
        onEvent = viewModel::onEvent,
        onNavigateBack = { navController.popBackStack() }
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditEstudianteBody(
    state: EditEstudianteUiState,
    onEvent: (EditEstudianteUiEvent) -> Unit,
    onNavigateBack: () -> Unit
){
    var estudianteParaEliminar by remember { mutableStateOf<Estudiante?>(null) }

    estudianteParaEliminar?.let { estudiante ->
        DeleteConfirmationDialog(
            estudiante = estudiante,
            onDismiss = { estudianteParaEliminar = null },
            onConfirm = {
                estudianteParaEliminar = null
                onEvent(EditEstudianteUiEvent.deleteEstudiante)
            }
        )
    }
    var topText =
        if(state.canBeDeleted){
            "Editar Estudiante"
        } else {
            "Nuevo Estudiante"
        }

    Scaffold(
        modifier =  Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {Text(topText)},
                windowInsets = WindowInsets(top = 0),
                navigationIcon = {
                    IconButton(onClick = onNavigateBack){
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Navegar Atrás"
                        )
                    }
                }
            )
        }
    ) {
        padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(all = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = state.nombre,
                onValueChange = {onEvent(EditEstudianteUiEvent.nombreChanged( it))},
                label = { Text("Nombre") },
                isError = state.nombreError != null,
                modifier = Modifier
                    .fillMaxWidth()
            )

            if(state.nombreError != null){
                Text(
                    text = state.nombreError,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            OutlinedTextField(
                value = state.carrera,
                onValueChange = {onEvent(EditEstudianteUiEvent.carreraChanged( it))},
                label = {Text("Carrera")},
                isError = state.carreraError != null,
                modifier = Modifier
                    .fillMaxWidth()
            )

            if(state.carreraError != null){
                Text(
                    text = state.carreraError,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            OutlinedTextField(
                value = state.edad,
                onValueChange = {onEvent(EditEstudianteUiEvent.edadChanged( it))},
                label = {Text("Edad")},
                isError = state.edadError != null,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .fillMaxWidth()
            )

            if(state.edadError != null){
                Text(
                    text = state.edadError,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ){
                Button(
                    onClick = {onEvent(EditEstudianteUiEvent.saveEstudiante)},
                    enabled = !state.isSaving,
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Text("Guardar")
                }

                if(state.canBeDeleted){
                    OutlinedButton(
                        onClick = {
                            state.estudianteId?.let { estudianteId ->
                                estudianteParaEliminar = Estudiante(
                                    estudianteId = estudianteId,
                                    nombre = state.nombre,
                                    carrera = state.carrera,
                                    edad = state.edad.toIntOrNull() ?: 0

                                )
                            }
                        },
                                enabled = !state.isDeleting,
                        modifier = Modifier
                            .weight(1f)

                    ) {
                        Text("Eliminar")
                    }
                }

            }
        }
    }

}

@Composable
fun DeleteConfirmationDialog(
    estudiante: Estudiante,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
){
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "Eliminar Estudiante ")
                },
        text = { Text(text = "¿Está seguro que desea eliminar al estudiante ${estudiante.nombre}?") },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Eliminar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}