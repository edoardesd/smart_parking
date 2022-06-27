package com.example.smartparking

import android.app.Application
import android.content.Context
import com.example.smartparking.data.db.RoomsDatabase
import com.example.smartparking.data.network.ConnectivityInterceptor
import com.example.smartparking.data.network.ConnectivityInterceptorImpl
import com.example.smartparking.data.network.GoogleAPIService
import com.example.smartparking.data.network.choice.DatabaseNetworkDataSource
import com.example.smartparking.data.network.choice.DatabaseNetworkDataSourceImpl
import com.example.smartparking.data.network.result.NavigationNetworkDataSource
import com.example.smartparking.data.network.result.NavigationNetworkDataSourceImpl
import com.example.smartparking.data.provider.LocationProvider
import com.example.smartparking.data.provider.LocationProviderImpl
import com.example.smartparking.data.repository.LocationRepository
import com.example.smartparking.data.repository.LocationRepositoryImpl
import com.example.smartparking.ui.parking.navigation.result.NavigationResultViewModelFactory
import com.google.android.gms.location.LocationServices
import com.jakewharton.threetenabp.AndroidThreeTen
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton


class SmartParkingApplication : Application(), KodeinAware {
    override val kodein = Kodein.lazy {
        import(androidXModule(this@SmartParkingApplication))

        bind() from singleton { RoomsDatabase(instance()) }
        bind() from singleton { instance<RoomsDatabase>().roomsDetailsDao() }
        bind<ConnectivityInterceptor>() with singleton { ConnectivityInterceptorImpl(instance()) }
        bind() from singleton { GoogleAPIService(instance())!! }
        bind<NavigationNetworkDataSource>() with singleton { NavigationNetworkDataSourceImpl(instance()) }
        bind() from provider { LocationServices.getFusedLocationProviderClient(instance<Context>()) }
        bind<LocationProvider>() with singleton { LocationProviderImpl(instance(), instance()) }
        bind<LocationRepository>() with singleton { LocationRepositoryImpl(instance(), instance()) }
//        bind<LocationProvider>() with singleton { LocationProviderImpl() }
        bind<DatabaseNetworkDataSource>() with singleton { DatabaseNetworkDataSourceImpl(instance()) }
        bind() from provider { NavigationResultViewModelFactory()}


    }

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
    }
}