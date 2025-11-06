package edu.ucne.esmailin_martinez_ap2_p2.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import edu.ucne.esmailin_martinez_ap2_p2.data.remote.repository.GastosRepositoryImpl
import edu.ucne.esmailin_martinez_ap2_p2.domain.repository.GastosRepository
import javax.inject.Singleton
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ProvideModule {

    @Binds
    @Singleton
    abstract fun bindGastosRepository(
        prioridadRepositoryImpl: GastosRepositoryImpl
    ): GastosRepository
}



