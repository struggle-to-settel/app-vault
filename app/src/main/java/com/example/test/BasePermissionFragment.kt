package com.example.test

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Looper
import android.provider.Settings
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding
import com.example.speedmatch.fragments.BaseFragment
import com.example.test.Common.showProgressDialog
import com.example.test.Common.showToast
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices


abstract class BasePermissionFragment<T : ViewBinding> : BaseFragment<T>() {

    private lateinit var mFusedLocationClient: FusedLocationProviderClient

    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", requireActivity().packageName, null)
            addCategory(Intent.CATEGORY_DEFAULT)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
        }
        startActivity(intent)
    }

    /**
     * Location Permission check */
    private fun ifShouldShowLocationRPermissionRequest(): Boolean {
        return (ActivityCompat.shouldShowRequestPermissionRationale(
            requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION
        )) || (ActivityCompat.shouldShowRequestPermissionRationale(
            requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION
        ))
    }


    @SuppressLint("MissingPermission", "SetTextI18n")
    fun getLocation() {
        if (isLocationPermissionGranted()) {
            if (isGPSOrNetworkEnabled()) {
                mFusedLocationClient =
                    LocationServices.getFusedLocationProviderClient(requireActivity())
                val mLocationRequest: LocationRequest = LocationRequest.create()
                mLocationRequest.interval = 60000
                mLocationRequest.fastestInterval = 5000
                mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                val mLocationCallback: LocationCallback = object : LocationCallback() {
                    override fun onLocationResult(locationResult: LocationResult) {
                        Log.d("onLocationResult", "last location : ${locationResult.lastLocation} ")
                        Log.d("onLocationResult", "locations : ${locationResult.locations} ")
                        if (locationResult.locations.isEmpty()) {
                            return
                        }
                        for (location in locationResult.locations) {
                            if (location != null) {
                                mFusedLocationClient.removeLocationUpdates(this)
                                onLocationChanged(location.latitude, location.longitude)
                                break
                            }
                        }
                    }
                }
                showProgressDialog(requireContext())
                mFusedLocationClient.requestLocationUpdates(
                    mLocationRequest, mLocationCallback, Looper.getMainLooper()
                )
            } else {
                "Please turn on location".showToast(requireContext())
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else if (ifShouldShowLocationRPermissionRequest()) {
            openAppSettings()
        } else {
            requestLocationPermissions()
        }

    }


    private fun isLocationPermissionGranted(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }


    private fun requestLocationPermissions() {
        locationPermissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION
            )
        )
    }

    open fun onLocationChanged(lat: Double, lng: Double) {}


    private fun isGPSOrNetworkEnabled(): Boolean {
        val locationManager: LocationManager = ContextCompat.getSystemService(
            requireContext(), LocationManager::class.java
        ) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }


    private val locationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            if (it[Manifest.permission.ACCESS_COARSE_LOCATION] == true || it[Manifest.permission.ACCESS_FINE_LOCATION] == true) getLocation()
        }


    /**
     * Media permission
     * */

    private fun isCameraGalleryPermissionGranted(): Boolean {

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.READ_MEDIA_AUDIO
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.READ_MEDIA_VIDEO
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.MANAGE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        }


    }

    private fun requestCameraGalleryPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            cameraGalleryPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_AUDIO,
                    Manifest.permission.READ_MEDIA_VIDEO,
                    Manifest.permission.MANAGE_EXTERNAL_STORAGE
                )
            )
        } else {
            cameraGalleryPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            )
        }
    }

    private fun ifShouldShowCameraGalleryPermissionRequest(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            (ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(), Manifest.permission.CAMERA
            )) || (ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE
            )) || (ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(), Manifest.permission.READ_MEDIA_IMAGES
            )) || (ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(), Manifest.permission.READ_MEDIA_AUDIO
            )) || (ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(), Manifest.permission.READ_MEDIA_VIDEO
            )) || (ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(), Manifest.permission.MANAGE_EXTERNAL_STORAGE
            ))
        } else {
            (ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(), Manifest.permission.CAMERA
            )) || (ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE
            )) || (ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE
            ))
        }
    }

    private val cameraGalleryPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (it[Manifest.permission.CAMERA] == true
                    && it[Manifest.permission.READ_MEDIA_IMAGES] == true
                    && it[Manifest.permission.READ_MEDIA_AUDIO] == true
                    && it[Manifest.permission.READ_MEDIA_VIDEO] == true
                ) mediaPermissionGranted()
            } else {
                if (it[Manifest.permission.CAMERA] == true &&
                    it[Manifest.permission.READ_EXTERNAL_STORAGE] == true
                    && it[Manifest.permission.WRITE_EXTERNAL_STORAGE] == true
                ) mediaPermissionGranted()
            }
        }

    fun checkCameraGalleryPermission() {
        if (isCameraGalleryPermissionGranted()) {
            mediaPermissionGranted()
        } else if (ifShouldShowCameraGalleryPermissionRequest()) {
            openAppSettings()
        } else {
            requestCameraGalleryPermission()
        }
    }

    open fun mediaPermissionGranted() {}


}