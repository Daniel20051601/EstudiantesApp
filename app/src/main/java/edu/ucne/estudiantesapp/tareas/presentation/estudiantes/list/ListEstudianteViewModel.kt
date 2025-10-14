package edu.ucne.estudiantesapp.tareas.presentation.estudiantes.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.estudiantesapp.domain.usecase.GetEstudiantesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListEstudianteViewModel @Inject constructor(
    private val getEstudiantesUseCase: GetEstudiantesUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(ListEstudianteUiState())
    val uiState: StateFlow<ListEstudianteUiState> = _uiState.asStateFlow()

    init {
        loadEstudiantes()
    }

    fun loadEstudiantes() {
        viewModelScope.launch {
            _uiState.update {state ->
                state.copy(isLoading = true)
            }
            getEstudiantesUseCase().collect { estudiantesList ->
                _uiState.update { state ->
                    state.copy(
                        estudiantes = estudiantesList,
                        isLoading = false
                    )
                }
            }
        }
    }
}