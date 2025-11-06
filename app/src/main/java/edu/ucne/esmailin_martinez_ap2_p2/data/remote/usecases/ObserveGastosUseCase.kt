package edu.ucne.esmailin_martinez_ap2_p2.data.remote.usecases

import edu.ucne.esmailin_martinez_ap2_p2.domain.model.Gastos
import edu.ucne.esmailin_martinez_ap2_p2.domain.repository.GastosRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveGastosUseCase @Inject constructor(
    private val repo: GastosRepository
) {
    operator fun invoke(): Flow<List<Gastos>> {
        return repo.getGastos()
    }
}
