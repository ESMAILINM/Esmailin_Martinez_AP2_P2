package edu.ucne.esmailin_martinez_ap2_p2.data.remote.usecases

import edu.ucne.esmailin_martinez_ap2_p2.domain.repository.GastosRepository
import javax.inject.Inject

class DeleteGastoUseCase @Inject constructor(
    private val GastosRepository: GastosRepository
) {
    suspend operator fun invoke(id: Int) {
        GastosRepository.deleteGastos(id)
    }
}
