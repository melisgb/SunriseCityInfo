package com.ggonzales.sunrisecityinfo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class ViewLocation : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    var latitude : Double? = null
    var longitude : Double? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_location)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val intentExtras = intent.extras!!
        latitude = intentExtras.getDouble("latitud")
        longitude = intentExtras.getDouble("longitud")
    }

    /**
     * Manipulates the map once available. From Google Maps API
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap!!.clear()

        // Add a marker in Sydney and move the camera
        val userLocation = LatLng(latitude!!, longitude!!)
        mMap.addMarker(MarkerOptions()
            .position(userLocation)
            .title("Location Selected"))
        //possible zoom from 0 - 24
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 5f))
    }
}
