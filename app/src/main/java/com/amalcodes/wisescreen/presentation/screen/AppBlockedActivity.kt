package com.amalcodes.wisescreen.presentation.screen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.appcompat.app.AppCompatActivity
import com.amalcodes.wisescreen.core.autoCleared
import com.amalcodes.wisescreen.core.getApplicationName
import com.amalcodes.wisescreen.databinding.ActivityAppBlockedBinding
import com.amalcodes.wisescreen.presentation.viewentity.AppBlockedType
import kotlinx.android.parcel.Parcelize

class AppBlockedActivity : AppCompatActivity() {

    companion object {

        private const val KEY_ARGS = "ARGS"

        fun getIntent(context: Context, args: AppBlockedActivityArgs): Intent {
            return Intent(context, AppBlockedActivity::class.java).apply {
                putExtra(KEY_ARGS, args)
            }
        }
    }

    private val args: AppBlockedActivityArgs by lazy {
        intent.getParcelableExtra<AppBlockedActivityArgs>(KEY_ARGS)
    }

    private var binding: ActivityAppBlockedBinding by autoCleared()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppBlockedBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.tvDescription.text = packageManager.getApplicationName(args.packageName)
        binding.btnOk.setOnClickListener {
            finishAndRemoveTask()
            val homeIntent = Intent(Intent.ACTION_MAIN)
                .addCategory(Intent.CATEGORY_HOME)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(homeIntent)
        }
    }
}

@Parcelize
data class AppBlockedActivityArgs(
    val packageName: String,
    @AppBlockedType
    val appBlockedType: Int
) : Parcelable