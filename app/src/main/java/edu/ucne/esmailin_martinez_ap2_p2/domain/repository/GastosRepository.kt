package edu.ucne.esmailin_martinez_ap2_p2.domain.repository

import edu.ucne.esmailin_martinez_ap2_p2.domain.model.Gastos
import kotlinx.coroutines.flow.Flow

interface GastosRepository {
    suspend fun getGasto(id: Int): Gastos?
    fun getGastos(): Flow<List<Gastos>>
    suspend fun insertGastos(gastos: Gastos)
    suspend fun updateGastos(gastos: Gastos)
    suspend fun deleteGastos(id: Int)
}