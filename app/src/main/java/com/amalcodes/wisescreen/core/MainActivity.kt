package com.amalcodes.wisescreen.core

import android.app.AppOpsManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import androidx.core.content.getSystemService
import com.amalcodes.wisescreen.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main) {


//	override fun onCreate(savedInstanceState: Bundle?) {
//		super.onCreate(savedInstanceState)
//		val list = repo.getAppUsage(UsageStatsManager.INTERVAL_DAILY)
//		println(isAppUsageStatsGranted())
//		if (!isAppUsageStatsGranted()) {
//			requestUsageStatsPermission()
//		} else {
//			list.forEach {
//			}
//		}

//	}

	fun requestUsageStatsPermission() {
		startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
	}

	private fun isAppUsageStatsGranted(): Boolean {
		val appOpsManager: AppOpsManager? = getSystemService()
		val mode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
			appOpsManager?.unsafeCheckOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, android.os.Process.myUid(), packageName)
		} else {
			@Suppress("DEPRECATION")
			appOpsManager?.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, android.os.Process.myUid(), packageName)
		}
		if (mode == AppOpsManager.MODE_DEFAULT) {
			return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
				checkCallingOrSelfPermission(android.Manifest.permission.PACKAGE_USAGE_STATS) == PackageManager.PERMISSION_GRANTED
			} else {
				true
			}
		}
		return mode == AppOpsManager.MODE_ALLOWED
	}
}