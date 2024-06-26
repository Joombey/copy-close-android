package dev.farukh.copyclose.core

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import db.CopyCloseDB
import dev.farukh.copyclose.MainViewModel
import dev.farukh.copyclose.core.data.repos.AuthRepository
import dev.farukh.copyclose.core.data.repos.FileRepository
import dev.farukh.copyclose.core.data.repos.OrderRepository
import dev.farukh.copyclose.core.data.repos.UserRepository
import dev.farukh.copyclose.core.data.source.UserLocalDataSource
import dev.farukh.copyclose.core.data.source.UserRemoteDataSource
import dev.farukh.copyclose.core.domain.GetOrderListUseCase
import dev.farukh.copyclose.core.utils.MediaManager
import dev.farukh.copyclose.core.utils.MediaManagerImpl
import dev.farukh.network.networkDI
import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.bindSingleton
import org.kodein.di.instance

internal fun coreDI(appDI: DI) = DI {
    extend(appDI)
    import(networkDI)

    bindProvider<SqlDriver> { AndroidSqliteDriver(CopyCloseDB.Schema, instance(), "db") }

    bindSingleton<CopyCloseDB> { CopyCloseDB(instance()) }

    bindProvider { UserLocalDataSource(instance()) }

    bindProvider { MediaManagerImpl(instance<Context>().contentResolver) }

    bindProvider<MediaManager> { instance<MediaManagerImpl>() }

    bindProvider { UserRemoteDataSource(instance(), instance(), instance(), instance(), instance()) }

    bindProvider { UserRepository(instance(), instance()) }

    bindProvider { AuthRepository(instance()) }

    bindProvider { FileRepository(instance(), instance()) }

    bindProvider { OrderRepository(instance(), instance()) }

    bindProvider {
        GetOrderListUseCase(
            orderRepository = instance(),
            userRepository = instance(),
            fileRepository = instance()
        )
    }

    //ViewModels
    bindProvider { MainViewModel( instance() ) }
}