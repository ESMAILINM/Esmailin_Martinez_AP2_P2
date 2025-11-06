package edu.ucne.esmailin_martinez_ap2_p2.domain.mapper

import edu.ucne.esmailin_martinez_ap2_p2.data.remote.dto.GastosDto
import edu.ucne.esmailin_martinez_ap2_p2.domain.model.Gastos


fun GastosDto.toDomain() = Gastos(
    gastoId = gastoId,
    fecha = fecha,
    suplidor = suplidor,
    ncf = ncf,
    itbis= itbis,
    monto= monto
)
fun Gastos.toDto() = GastosDto(
    gastoId = gastoId,
    fecha = fecha,
    suplidor = suplidor,
    ncf = ncf,
    itbis= itbis,
    monto= monto
)