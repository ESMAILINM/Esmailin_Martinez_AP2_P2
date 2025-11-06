package edu.ucne.esmailin_martinez_ap2_p2.presentacion.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.esmailin_martinez_ap2_p2.data.remote.Resource
import edu.ucne.esmailin_martinez_ap2_p2.data.remote.usecases.DeleteGastoUseCase
import edu.ucne.esmailin_martinez_ap2_p2.data.remote.usecases.InsertGastoUseCase
import edu.ucne.esmailin_martinez_ap2_p2.data.remote.usecases.ObserveGastosUseCase
import edu.ucne.esmailin_martinez_ap2_p2.data.remote.usecases.UpdateGastoUseCase
import edu.ucne.esmailin_martinez_ap2_p2.domain.model.Gastos
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GastosViewModel @Inject constructor(
    private val deleteGastoUseCase: DeleteGastoUseCase,
    private val observeGastosUseCase: ObserveGastosUseCase,
    private val insertGastoUseCase: InsertGastoUseCase,
    private val updateGastoUseCase: UpdateGastoUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(GastosUiState(fecha = "", itbis = 0.0, monto = 0.0, ncf = "", suplidor = ""))
    val uiState: StateFlow<GastosUiState> = _uiState.asStateFlow()

    init {
        obtenerGastos()
    }

    private fun obtenerGastos() {
        viewModelScope.launch {
            observeGastosUseCase()
                .onStart { _uiState.update { it.copy(loading = true, error = null) } }
                .catch { e -> _uiState.update { it.copy(loading = false, error = e.message ?: "Error al obtener gastos") } }
                .collectLatest { lista ->
                    _uiState.update { it.copy(gastos = lista, loading = false, error = null) }
                }
        }
    }

    fun onEvent(event: GastosUiEvent) {
        when (event) {
            is GastosUiEvent.suplidorChanged -> _uiState.update { it.copy(suplidor = event.value) }
            is GastosUiEvent.fechaChanged -> _uiState.update { it.copy(fecha = event.value) }
            is GastosUiEvent.ncfChanged -> _uiState.update { it.copy(ncf = event.value) }
            is GastosUiEvent.itbisChanged -> _uiState.update { it.copy(itbis = event.value) }
            is GastosUiEvent.montoChanged -> _uiState.update { it.copy(monto = event.value) }
            is GastosUiEvent.Load -> loadGastos(event.id)
            is GastosUiEvent.Save -> saveGastos()
            is GastosUiEvent.Delete -> deleteGastos()
            is GastosUiEvent.showSheet -> _uiState.update { it.copy(showSheet = true) }
            is GastosUiEvent.hideSheet -> _uiState.update {
                it.copy(showSheet = false, suplidor = "", gastoId = 0, fecha = "", ncf = "", itbis = 0.0, monto = 0.0)
            }
            is GastosUiEvent.userMessageShown -> _uiState.update { it.copy(userMessage = null) }
        }
    }

    private suspend fun <T> safeCall(action: suspend () -> T): Resource<T> {
        return try {
            Resource.Success(action())
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error desconocido")
        }
    }

    private fun saveGastos() {
        val gasto = Gastos(
            gastoId = _uiState.value.gastoId,
            fecha = _uiState.value.fecha,
            suplidor = _uiState.value.suplidor,
            ncf = _uiState.value.ncf,
            itbis = _uiState.value.itbis,
            monto = _uiState.value.monto
        )

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }

            val result = safeCall {
                if (gasto.gastoId == 0) insertGastoUseCase(gasto)
                else updateGastoUseCase(gasto)
            }

            when (result) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            isSaving = false,
                            userMessage = if (gasto.gastoId == 0) "Gasto guardado exitosamente" else "Gasto actualizado exitosamente",
                            showSheet = false,
                            gastoId = 0,
                            fecha = "",
                            suplidor = "",
                            ncf = "",
                            itbis = 0.0,
                            monto = 0.0
                        )
                    }
                    obtenerGastos()
                }
                is Resource.Error -> _uiState.update { it.copy(isSaving = false, userMessage = result.message) }
                is Resource.Loading -> _uiState.update { it.copy(isSaving = true) }
            }
        }
    }

    fun loadGastos(id: Int) {
        _uiState.value.gastos.find { (it.gastoId ?: 0) == id }?.let { gasto ->
            _uiState.update {
                it.copy(
                    gastoId = gasto.gastoId ?: 0,
                    fecha = gasto.fecha,
                    suplidor = gasto.suplidor,
                    ncf = gasto.ncf,
                    itbis = gasto.itbis,
                    monto = gasto.monto,
                    isNew = false
                )
            }
        }
    }

    private fun deleteGastos() {
        val id = _uiState.value.gastoId
        if (id == 0) {
            _uiState.update { it.copy(userMessage = "Seleccione un Gasto para eliminar") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isDeleting = true) }

            val result = safeCall { deleteGastoUseCase(id) }

            when (result) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            isDeleting = false,
                            userMessage = "Gasto eliminado correctamente",
                            suplidor = "",
                            gastoId = 0,
                            fecha = "",
                            ncf = "",
                            itbis = 0.0,
                            monto = 0.0,
                            showSheet = false
                        )
                    }
                    obtenerGastos()
                }
                is Resource.Error -> _uiState.update { it.copy(isDeleting = false, userMessage = result.message) }
                is Resource.Loading -> _uiState.update { it.copy(isDeleting = true) }
            }
        }
    }
}
