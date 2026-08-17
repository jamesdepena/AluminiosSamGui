package edu.ucne.aluminiossamgui.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import edu.ucne.aluminiossamgui.data.local.dao.TrabajoDao
import edu.ucne.aluminiossamgui.data.local.dao.HuecoDao
import edu.ucne.aluminiossamgui.data.db.AluminiosSamGuiDatabase
import edu.ucne.aluminiossamgui.data.repository.TrabajoRepositoryImpl
import edu.ucne.aluminiossamgui.data.repository.HuecoRepositoryImpl
import edu.ucne.aluminiossamgui.domain.repository.TrabajoRepository
import edu.ucne.aluminiossamgui.domain.repository.HuecoRepository
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context) =
         Room.databaseBuilder(
            appContext,
            AluminiosSamGuiDatabase::class.java,
            "aluminios_samgui_database"
        ).fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideTrabajoDao(database: AluminiosSamGuiDatabase): TrabajoDao {
        return database.trabajoDao()
    }

    @Provides
    @Singleton
    fun provideHuecoDao(database: AluminiosSamGuiDatabase): HuecoDao {
        return database.huecoDao()
    }

    @Provides
    @Singleton
    fun provideTrabajoRepositoryImpl(trabajoDao: TrabajoDao): TrabajoRepositoryImpl {
        return TrabajoRepositoryImpl(trabajoDao)
    }

    @Provides
    @Singleton
    fun provideTrabajoRepository(impl: TrabajoRepositoryImpl): TrabajoRepository {
        return impl
    }

    @Provides
    @Singleton
    fun provideHuecoRepositoryImpl(huecoDao: HuecoDao): HuecoRepositoryImpl {
        return HuecoRepositoryImpl(huecoDao)
    }

    @Provides
    @Singleton
    fun provideHuecoRepository(impl: HuecoRepositoryImpl): HuecoRepository {
        return impl
    }
}