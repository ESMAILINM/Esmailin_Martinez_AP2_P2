package edu.ucne.esmailin_martinez_ap2_p2.data.remote.usecases

import edu.ucne.esmailin_martinez_ap2_p2.domain.model.Gastos
import edu.ucne.esmailin_martinez_ap2_p2.domain.repository.GastosRepository
import javax.inject.Inject


class InsertGastoUseCase @Inject constructor(
    private val GastosRepository: GastosRepository
) {
    suspend operator fun invoke(gastos: Gastos) {
        GastosRepository.insertGastos(gastos)
    }
}