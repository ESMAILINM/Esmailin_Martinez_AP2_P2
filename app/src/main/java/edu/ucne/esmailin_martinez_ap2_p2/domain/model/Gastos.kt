package edu.ucne.esmailin_martinez_ap2_p2.domain.model

data class Gastos(
    val gastoId: Int?,
    val fecha: String,
    val suplidor: String,
    val ncf : String,
    val itbis: Double,
    val monto: Double
)
