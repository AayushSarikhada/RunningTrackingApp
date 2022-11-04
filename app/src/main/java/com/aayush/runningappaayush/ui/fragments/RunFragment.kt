package com.aayush.runningappaayush.ui.fragments

import android.Manifest
import android.app.Instrumentation.ActivityResult
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.aayush.runningappaayush.R
import com.aayush.runningappaayush.other.Constants.REQUEST_CODE_CONSTANT_BACKGROUND_PERMISSION
import com.aayush.runningappaayush.other.Constants.REQUEST_CODE_CONSTANT_PERMISSION
import com.aayush.runningappaayush.other.Utils
import com.aayush.runningappaayush.ui.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_run.*
import kotlinx.android.synthetic.main.fragment_setup.*
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

@AndroidEntryPoint
class RunFragment:Fragment(R.layout.fragment_run),EasyPermissions.PermissionCallbacks {

    private val viewModel:MainViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestPermission()

        fab.setOnClickListener {
            findNavController().navigate(R.id.action_runFragment_to_trackingFragment)
        }
    }

    private fun requestBackgroundPermission(){
        if(Utils.hasBackgroundPermission(requireContext())){
            return
        }else{
            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.Q){
                EasyPermissions.requestPermissions(this,
                    "Without background location permission also this app won't work",
                    REQUEST_CODE_CONSTANT_BACKGROUND_PERMISSION,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                )
            }
        }
    }
    private fun requestPermission(){
        if(Utils.hasLocationPermissions(requireContext())){
            return
        }else{
            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q){
                EasyPermissions.requestPermissions(this,
                    "Without location permission this app won't work",
                    REQUEST_CODE_CONSTANT_PERMISSION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                    )
            }else{
                EasyPermissions.requestPermissions(this,
                    "Without location permission this app won't work",
                    REQUEST_CODE_CONSTANT_PERMISSION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )

//                EasyPermissions.requestPermissions(this,
//                "Without Location permission this app wont work sorry",
//                    REQUEST_CODE_CONSTANT_PERMISSION,
//                    Manifest.permission.ACCESS_BACKGROUND_LOCATION)
            }
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {

        Log.d("PERMISSIONS","GRANTED -> request code: $requestCode and permission: ${perms.toString()}")
        requestBackgroundPermission()
    }


    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        Log.d("PERMISSIONS","DENIED -> request code: $requestCode and permission: ${perms.toString()}")
        if(EasyPermissions.somePermissionPermanentlyDenied(this,perms)){
            AppSettingsDialog.Builder(this).build().show()
        }else{
            requestPermission()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,this)
    }

}