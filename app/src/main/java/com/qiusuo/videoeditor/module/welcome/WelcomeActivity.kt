package com.qiusuo.videoeditor.module.welcome

import android.Manifest
import android.content.Intent
import android.os.Bundle
import com.qiusuo.videoeditor.base.BaseActivity
import com.qiusuo.videoeditor.databinding.ActivityWelcomeBinding
import com.qiusuo.videoeditor.module.home.MainActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.PermissionRequest

class WelcomeActivity : BaseActivity<ActivityWelcomeBinding>(ActivityWelcomeBinding::inflate), EasyPermissions.PermissionCallbacks {
    val STORAGE_PERMISSIONS = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
    val CAMERA_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    val REQUEST_STORAGE_PREMISSION = 2000
    val REQUEST_CAMERA_PREMISSION = 3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun initData() {
        if (!EasyPermissions.hasPermissions(this, *STORAGE_PERMISSIONS)) {
            EasyPermissions.requestPermissions(PermissionRequest.Builder(this, REQUEST_STORAGE_PREMISSION, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .build())
        } else {
            toMain()
        }
    }

    fun toMain() {
        GlobalScope.launch {
            delay(1000);
            startActivity(Intent(this@WelcomeActivity, MainActivity::class.java))
            finish()
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        finish()
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        toMain()
    }

}