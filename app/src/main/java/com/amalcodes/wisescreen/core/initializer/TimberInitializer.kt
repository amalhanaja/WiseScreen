package com.amalcodes.wisescreen.core.initializer

import android.content.Context
import androidx.startup.Initializer
import com.amalcodes.wisescreen.BuildConfig
import timber.log.Timber

/**
 * @author: AMAL
 * Created On : 18/07/20
 */


class TimberInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        val tree = if (BuildConfig.DEBUG) {
            Timber.DebugTree()
        } else {
            object : Timber.Tree() {
                override fun log(
                    priority: Int,
                    tag: String?,
                    message: String,
                    t: Throwable?
                ) {
                    // No OP
                }
            }
        }
        Timber.plant(tree)
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}