package edu.ucne.estudiantesapp.tareas.presentation.estudiantes.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.estudiantesapp.domain.model.Estudiante
import edu.ucne.estudiantesapp.domain.usecase.DeleteEstudianteUseCase
import edu.ucne.estudiantesapp.domain.usecase.GetEstudianteUseCase
import edu.ucne.estudiantesapp.domain.usecase.PostEstudianteUseCase
import edu.ucne.estudiantesapp.domain.usecase.PutEstudianteUseCase
import edu.ucne.estudiantesapp.domain.validation.EstudianteValidator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditEstudianteViewModel @Inject constructor(
    val getEstudianteUseCase: GetEstudianteUseCase,
    val deleteEstudianteUseCase: DeleteEstudianteUseCase,
    val postEstudianteUseCase: PostEstudianteUseCase,
    val putEstudianteUseCase: PutEstudianteUseCase,
    val validator : EstudianteValidator
): ViewModel() {
    private val _state = MutableStateFlow(EditEstudianteUiState())
    val state: StateFlow<EditEstudianteUiState> = _state.asStateFlow()

    fun onEvent(event: EditEstudianteUiEvent) {
        when(event) {
            is EditEstudianteUiEvent.loadEstudiante -> onLoad(event.id)
            is EditEstudianteUiEvent.nombreChanged -> _state.update { state ->
                state.copy(
                    nombre = event.nombre,
                    nombreError = null
                )
            }
            is EditEstudianteUiEvent.carreraChanged -> _state.update { state ->
                state.copy(
                    carrera = event.carrera,
                    carreraError = null
                )
            }
            is EditEstudianteUiEvent.edadChanged -> _state.update { state ->
                state.copy(
                    edad = event.edad,
                    edadError = null
                )
            }
            EditEstudianteUiEvent.saveEstudiante -> onSave()
            EditEstudianteUiEvent.deleteEstudiante -> onDelete()
        }
    }

    private fun onLoad(id: Int?) {
        if (id == null || id == 0){
            _state.update{state ->
                state.copy(
                    isLoading = false,
                    estudianteId = null,
                    nombre = "",
                    carrera = "",
                    edad = "",
                    errorMessage = null,
                    isSaved = false,
                    isSaving = false,
                    isDeleting = false,
                    canBeDeleted = false
                )
            }
            return
        }
        _state.update {state ->  state.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            try{
                val estudiante = getEstudianteUseCase(id)
                _state.update {state ->
                    if(estudiante != null){
                        state.copy(
                            estudianteId = estudiante.estudianteId,
                            nombre = estudiante.nombre,
                            carrera = estudiante.carrera,
                            edad = estudiante.edad.toString(),
                            isLoading = false,
                            canBeDeleted = true,
                            errorMessage = null
                        )
                    }else{
                        state.copy(
                            isLoading = false,
                            errorMessage = "Estudiante no encontrado",
                            estudianteId = null,
                            nombre = "",
                            carrera = "",
                            edad = "",
                            canBeDeleted = false
                        )
                    }

                }
            }catch (e: Exception){
                _state.update {state ->
                    state.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Error desconocido al cargar el estudiante",
                        canBeDeleted = false
                    )

                }

            }
        }

    }

    private fun onSave() {
        val nombre = _state.value.nombre
        val carrera = _state.value.carrera
        val edadStr = _state.value.edad

        viewModelScope.launch {
            _state.update { state -> state.copy(isSaving = true, errorMessage = null, nombreError = null, carreraError = null, edadError = null) }
            val validateNombre = validator.validateNombre(nombre)
            val validateCarrera = validator.validateCarrera(carrera)
            val validateEdad = validator.validateEdad(edadStr)
            if (!validateNombre.isValid || !validateCarrera.isValid || !validateEdad.isValid) {
                _state.update { state ->
                    state.copy(
                        isSaving = false,
                        nombreError = validateNombre.errorMessage,
                        carreraError = validateCarrera.errorMessage,
                        edadError = validateEdad.errorMessage,
                        errorMessage = "Por favor corrija los errores antes de guardar."
                    )
                }
                return@launch
            }
            val id = _state.value.estudianteId ?: 0

            val estudiante = Estudiante(
                estudianteId = id,
                nombre = nombre,
                carrera = carrera,
                edad = edadStr.toInt()
            )

            if (id == 0) {
                val result = postEstudianteUseCase(estudiante)
                result.onSuccess { nuevoEstudiante ->
                    _state.update { state ->
                        state.copy(
                            isSaving = false,
                            isSaved = true,
                            estudianteId = nuevoEstudiante.estudianteId,
                            errorMessage = null
                        )
                    }
                }.onFailure { e ->
                    _state.update { state ->
                        state.copy(
                            isSaving = false,
                            errorMessage = e.message ?: "Error desconocido al guardar el estudiante"
                        )
                    }
                }
            } else {
                val result = putEstudianteUseCase(id, estudiante)
                result.onSuccess { isSuccess ->
                    _state.update { state ->
                        state.copy(
                            isSaving = false,
                            isSaved = isSuccess,
                            errorMessage = null
                        )
                    }
                }.onFailure { e ->
                    _state.update { state ->
                        state.copy(
                            isSaving = false,
                            errorMessage = e.message ?: "Error desconocido al guardar el estudiante"
                        )
                    }
                }
            }
        }
    }


    private fun onDelete() {
        val estudianteId  = _state.value.estudianteId
        if(estudianteId == null || estudianteId == 0){
            _state.update {state ->
                state.copy(errorMessage = "No se puede eliminar un estudiante no guardado.")
            }
            return
        }

        _state.update{state ->
            state.copy(
                isDeleting = true,
                errorMessage = null,
                isSaved = false
            )
        }

        viewModelScope.launch {
            try{
                deleteEstudianteUseCase(estudianteId)
                _state.update { state ->
                    state.copy(
                        isDeleting = false,
                        isSaved = true,
                        estudianteId = null,
                        nombre = "",
                        carrera = "",
                        edad = "",
                        canBeDeleted = false,
                        errorMessage = null
                    )
                }
            }catch (e: Exception){
                _state.update { state ->
                    state.copy(
                        isDeleting = false,
                        errorMessage = e.message ?: "Error desconocido al eliminar el estudiante"
                    )
                }
            }
        }
    }


}