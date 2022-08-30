package rekab.app.background_locator.provider

import android.annotation.SuppressLint
import android.content.Context
import android.os.PowerManager
import com.google.android.gms.location.*

class GoogleLocationProviderClient(context: Context,powerManager: PowerManager, override var listener: LocationUpdateListener?) : BLLocationProvider {
    private val client: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    private val locationCallback = LocationListener(listener, powerManager)

    override fun removeLocationUpdates() {
        client.removeLocationUpdates(locationCallback)
    }

    @SuppressLint("MissingPermission")
    override fun requestLocationUpdates(request: LocationRequestOptions) {
        client.requestLocationUpdates(getLocationRequest(request), locationCallback, null)
    }

    private fun getLocationRequest(request: LocationRequestOptions): LocationRequest {
        val locationRequest = LocationRequest.create()

        locationRequest.interval = request.interval
        locationRequest.fastestInterval = request.interval
        locationRequest.maxWaitTime = request.interval
        locationRequest.priority = request.accuracy
        locationRequest.smallestDisplacement = request.distanceFilter

        return locationRequest
    }
}

private class LocationListener(val listener: LocationUpdateListener?, powerManager: PowerManager) : LocationCallback() {
    private val pm = powerManager
    
    override fun onLocationResult(location: LocationResult?) {
        listener?.onLocationUpdated(LocationParserUtil.getLocationMapFromLocation(location, pm))
    }
}
