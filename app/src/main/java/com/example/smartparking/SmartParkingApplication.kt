package com.example.smartparking

import android.app.Application
import com.example.smartparking.data.db.RoomsDatabase
import com.example.smartparking.data.network.ConnectivityInterceptor
import com.example.smartparking.data.network.ConnectivityInterceptorImpl
import com.example.smartparking.data.network.GoogleAPIService
import com.example.smartparking.data.network.choice.DatabaseNetworkDataSource
import com.example.smartparking.data.network.choice.DatabaseNetworkDataSourceImpl
import com.example.smartparking.data.network.result.NavigationNetworkDataSource
import com.example.smartparking.data.network.result.NavigationNetworkDataSourceImpl
import com.example.smartparking.data.repository.LocationRepository
import com.example.smartparking.data.repository.LocationRepositoryImpl
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton


class SmartParkingApplication : Application(), KodeinAware {
    override val kodein = Kodein.lazy {
        import(androidXModule(this@SmartParkingApplication))

        bind() from singleton { RoomsDatabase(instance()) }
        bind() from singleton { instance<RoomsDatabase>().roomsDetailsDao() }
        bind<ConnectivityInterceptor>() with singleton { ConnectivityInterceptorImpl(instance()) }
        bind() from singleton { GoogleAPIService(instance())!! }
        bind<NavigationNetworkDataSource>() with singleton { NavigationNetworkDataSourceImpl(instance()) }
        bind<LocationRepository>() with singleton { LocationRepositoryImpl(instance(), instance()) }
        bind<DatabaseNetworkDataSource>() with singleton { DatabaseNetworkDataSourceImpl(instance()) }

    }
}