package com.easybuilder.base.location

import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.easybuilder.common.utils.log

/**
 * MyLocationManager
 * Created by sky.Ren on 2024/11/4.
 * Description:
 */
class MyLocationManager {
    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener

    public fun getLocation(context: Context) {
        locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        locationListener = object : LocationListener {
            @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
            override fun onLocationChanged(location: Location) {
                // 处理位置更新
                val latitude = location.latitude
                val longitude = location.longitude
                // 例如，显示在 TextView 中
                "Latitude: $latitude, Longitude: $longitude".log()
            }

            override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {}
        }

        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, 0f, locationListener)
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    fun onDestroy() {
        if (::locationManager.isInitialized && ::locationListener.isInitialized) {
            locationManager.removeUpdates(locationListener)
        }
    }

}