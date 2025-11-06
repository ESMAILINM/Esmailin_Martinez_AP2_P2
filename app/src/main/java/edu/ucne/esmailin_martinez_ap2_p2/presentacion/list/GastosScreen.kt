package edu.ucne.esmailin_martinez_ap2_p2.presentacion.list

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import edu.ucne.esmailin_martinez_ap2_p2.domain.model.Gastos
import java.util.*

@Composable
fun GastosScreen(
    navController: NavController,
    viewModel: GastosViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    ListGastoBody(state, viewModel::onEvent)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListGastoBody(
    state: GastosUiState,
    onEvent: (GastosUiEvent) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(state.userMessage) {
        state.userMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            onEvent(GastosUiEvent.userMessageShown)
        }
    }

    LaunchedEffect(state.showSheet) {
        if (state.showSheet) sheetState.show() else sheetState.hide()
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onEvent(GastosUiEvent.showSheet) },
                modifier = Modifier.testTag("fab_add")
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar gasto")
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            when {
                state.loading -> CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .testTag("loading")
                )
                state.gastos.isEmpty() -> Text(
                    text = "No hay gastos registradas",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .testTag("empty_message"),
                    style = MaterialTheme.typography.bodyLarge
                )
                else -> LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.gastos, key = { it.gastoId ?: it.hashCode() }) { gasto ->
                        GastosItem(
                            gastos = gasto,
                            onClick = {
                                onEvent(GastosUiEvent.Load(gasto.gastoId ?: 0))
                                onEvent(GastosUiEvent.showSheet)
                            }
                        )
                    }
                }
            }
        }

        if (state.showSheet) {
            ModalBottomSheet(
                onDismissRequest = { onEvent(GastosUiEvent.hideSheet) },
                sheetState = sheetState
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .navigationBarsPadding()
                        .imePadding(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = if (state.isNew) "Nuevo Gasto" else "Editar Gasto",
                        style = MaterialTheme.typography.headlineSmall
                    )

                    OutlinedTextField(
                        value = state.suplidor,
                        onValueChange = { onEvent(GastosUiEvent.suplidorChanged(it)) },
                        label = { Text("Suplidor") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_suplidor"),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = state.fecha,
                        onValueChange = { onEvent(GastosUiEvent.fechaChanged(it)) },
                        label = { Text("Fecha") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_fecha"),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = state.ncf,
                        onValueChange = { onEvent(GastosUiEvent.ncfChanged(it)) },
                        label = { Text("NCF") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_ncf"),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = if (state.itbis == 0.0) "" else state.itbis.toString(),
                        onValueChange = { input ->
                            val value = input.toDoubleOrNull() ?: 0.0
                            onEvent(GastosUiEvent.itbisChanged(value))
                        },
                        label = { Text("ITBIS") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_itbis"),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = if (state.monto == 0.0) "" else state.monto.toString(),
                        onValueChange = { input ->
                            val value = input.toDoubleOrNull() ?: 0.0
                            onEvent(GastosUiEvent.montoChanged(value))
                        },
                        label = { Text("Monto") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_monto"),
                        singleLine = true
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = { onEvent(GastosUiEvent.hideSheet) },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Cancelar")
                        }
                        Button(
                            onClick = { if (state.suplidor.isNotBlank()) onEvent(GastosUiEvent.Save) },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("btn_save"),
                            enabled = state.suplidor.isNotBlank()
                        ) {
                            Text("Guardar")
                        }
                    }

                    if (!state.isNew) {
                        Button(
                            onClick = { onEvent(GastosUiEvent.Delete) },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Eliminar", color = MaterialTheme.colorScheme.onError)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GastosItem(
    gastos: Gastos,
    onClick: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("gastos_item_${gastos.gastoId ?: gastos.hashCode()}")
            .clickable { onClick() }
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(gastos.suplidor, style = MaterialTheme.typography.bodyLarge)
            gastos.gastoId?.takeIf { it != 0 }?.let {
                Text("ID: $it", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewGasto() {
    MaterialTheme {
        ListGastoBody(
            state = GastosUiState(
                loading = false,
                gastos = listOf(
                    Gastos(1, fecha = "", itbis = 0.0, monto=0.0,ncf ="", suplidor = ""),
                    Gastos(2, fecha = "", itbis = 0.0, monto=0.0,ncf ="", suplidor = "")
                ),
                gastoId = 0,
                suplidor = "",
                fecha = "",
                ncf = "",
                itbis= 0.0,
                monto= 0.0,
                showSheet = false,
                isNew = true,
                isSaving = false,
                isDeleting = false,
                userMessage = null,
                error = null
            )
        ) {}
    }
}
