package edu.ucne.esmailin_martinez_ap2_p2.data.remote.usecases

import edu.ucne.esmailin_martinez_ap2_p2.domain.model.Gastos
import edu.ucne.esmailin_martinez_ap2_p2.domain.repository.GastosRepository
import javax.inject.Inject

class GetGastoUseCase @Inject constructor(
    private val repo: GastosRepository
) {
    suspend operator fun invoke(id: Int): Gastos? {
        return repo.getGasto(id)
    }
}
