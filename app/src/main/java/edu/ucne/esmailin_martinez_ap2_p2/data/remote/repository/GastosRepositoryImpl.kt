package edu.ucne.esmailin_martinez_ap2_p2.data.remote.repository

import dagger.hilt.android.HiltAndroidApp
import edu.ucne.esmailin_martinez_ap2_p2.data.remote.GastosApi
import edu.ucne.esmailin_martinez_ap2_p2.domain.mapper.toDomain
import edu.ucne.esmailin_martinez_ap2_p2.domain.mapper.toDto
import edu.ucne.esmailin_martinez_ap2_p2.domain.model.Gastos
import edu.ucne.esmailin_martinez_ap2_p2.domain.repository.GastosRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject



 class GastosRepositoryImpl @Inject constructor(
    private val api: GastosApi
) : GastosRepository {

    override fun getGastos(): Flow<List<Gastos>> = flow {
        val result = api.getGastos().map { it.toDomain() }
        emit(result)
    }
    override suspend fun getGasto(id: Int): Gastos? {
        val dto = api.getGasto(id)
        return dto?.toDomain()
    }
    override suspend fun insertGastos(gastos: Gastos) {
        api.postGastos(gastos.toDto())
    }

    override suspend fun updateGastos(gastos: Gastos) {
        val id = gastos.gastoId
        if (id == 0) throw IllegalArgumentException("ID inválido para actualizar")
        api.updateGastos(id ?: 0, gastos.toDto())
    }

    override suspend fun deleteGastos(id: Int) {
        api.deleteGastos(id)
    }

}


