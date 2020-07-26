package com.amalcodes.wisescreen.presentation.viewentity

import android.os.Parcelable
import com.amalcodes.ezrecyclerview.adapter.entity.ItemEntity
import com.amalcodes.wisescreen.R
import com.amalcodes.wisescreen.domain.entity.AppLimitType
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

/**
 * @author: AMAL
 * Created On : 25/07/20
 */

@Parcelize
data class AppLimitViewEntity(
    val id: Long,
    val packageName: String,
    val type: AppLimitType,
    val limitTimeInMillis: Int
) : ItemEntity, Parcelable {

    @IgnoredOnParcel
    override val layoutRes: Int
        get() = R.layout.item_app_limit
}