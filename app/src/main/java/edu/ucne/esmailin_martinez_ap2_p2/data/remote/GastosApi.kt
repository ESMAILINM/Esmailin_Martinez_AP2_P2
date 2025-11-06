package edu.ucne.esmailin_martinez_ap2_p2.data.remote

import edu.ucne.esmailin_martinez_ap2_p2.data.remote.dto.GastosDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path


interface GastosApi {

    @GET ("api/Gastos")
    suspend fun getGastos(): List<GastosDto>
    @GET ("api/Gastos/{id}")
    suspend fun getGasto(@Path("id") id: Int): GastosDto?
    @POST("api/Gastos")
    suspend fun postGastos(@Body gastos: GastosDto): GastosDto

    @PUT("api/Gastos/{id}")
    suspend fun updateGastos(@Path("id") id: Int, @Body gastos: GastosDto): Response<Unit>

    @DELETE("api/Gastos/{id}")
    suspend fun deleteGastos(@Path("id") id: Int) : Response<Unit>
}