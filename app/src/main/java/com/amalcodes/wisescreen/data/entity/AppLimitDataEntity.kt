package com.amalcodes.wisescreen.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @author: AMAL
 * Created On : 25/07/20
 */

@Entity(tableName = "APPS_LIMIT")
class AppLimitDataEntity(
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val packageName: String,
    val limitTimeInMillis: Int,
    val type: String
)