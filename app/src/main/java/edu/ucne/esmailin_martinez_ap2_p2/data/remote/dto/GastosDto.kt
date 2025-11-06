package edu.ucne.esmailin_martinez_ap2_p2.data.remote.dto

import com.squareup.moshi.JsonClass

//data
@JsonClass(generateAdapter = true)
data class GastosDto (
    val gastoId: Int?,
    val fecha: String,
    val suplidor: String,
    val ncf : String,
    val itbis: Double,
    val monto: Double

)
