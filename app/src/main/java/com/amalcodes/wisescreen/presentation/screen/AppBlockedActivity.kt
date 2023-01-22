package com.amalcodes.wisescreen.presentation.screen

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import com.amalcodes.wisescreen.core.getApplicationName
import com.amalcodes.wisescreen.domain.entity.AppBlockedType
import com.amalcodes.wisescreen.features.blocked.AppBlockedPage
import com.amalcodes.wisescreen.features.blocked.AppBlockedPageState
import com.amalcodes.wisescreen.presentation.foundation.AppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.parcelize.Parcelize

@AndroidEntryPoint
class AppBlockedActivity : ComponentActivity() {

    companion object {

        private const val KEY_ARGS = "ARGS"

        fun getIntent(context: Context, args: AppBlockedActivityArgs): Intent {
            return Intent(context, AppBlockedActivity::class.java).apply {
                putExtra(KEY_ARGS, args)
            }
        }
    }

    private val args: AppBlockedActivityArgs? by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return@lazy intent.extras?.getParcelable(KEY_ARGS, AppBlockedActivityArgs::class.java)
        }
        return@lazy intent.extras?.getParcelable(KEY_ARGS) as? AppBlockedActivityArgs
    }

    @ExperimentalMaterial3Api
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val notNullArgs = requireNotNull(args)
        setContent {
            AppTheme {
                AppBlockedPage(
                    appBlockedPageState = AppBlockedPageState(
                        appBlockedType = notNullArgs.appBlockedType,
                        appName = packageManager.getApplicationName(notNullArgs.packageName)
                    ),
                    goToMain = {
                        finishAndRemoveTask()
                        val homeIntent = Intent(Intent.ACTION_MAIN)
                            .addCategory(Intent.CATEGORY_HOME)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(homeIntent)
                    }
                )
            }
        }
    }
}

@Parcelize
data class AppBlockedActivityArgs(
    val packageName: String,
    val appBlockedType: AppBlockedType,
) : Parcelable