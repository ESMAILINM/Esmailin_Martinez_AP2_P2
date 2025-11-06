package edu.ucne.esmailin_martinez_ap2_p2.presentacion.list

import edu.ucne.esmailin_martinez_ap2_p2.domain.model.Gastos


data class GastosUiState(
    val gastoId: Int = 0,
    val fecha: String,
    val suplidor: String,
    val ncf : String,
    val itbis: Double,
    val monto: Double,
    val isNew: Boolean = true,
    val isSaving: Boolean = false,
    val isDeleting: Boolean = false,
    val saved: Boolean = false,
    val deleted: Boolean = false,
    val error: String? = null,
    val loading: Boolean = false,
    val showSheet: Boolean = false,
    val userMessage: String? = null,
    val gastos: List<Gastos> = emptyList()
)
