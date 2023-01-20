package com.amalcodes.wisescreen.features.applimit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amalcodes.wisescreen.core.ApplicationInfoProvider
import com.amalcodes.wisescreen.domain.entity.AppLimitEntity
import com.amalcodes.wisescreen.domain.entity.AppLimitType
import com.amalcodes.wisescreen.domain.usecase.GetAppLimit
import com.amalcodes.wisescreen.domain.usecase.GetApplicationList
import com.amalcodes.wisescreen.domain.usecase.UseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/**
 * @author: AMAL
 * Created On : 25/07/20
 */


@ExperimentalCoroutinesApi
@HiltViewModel
class AppLimitViewModel @Inject constructor(
    getApplicationList: GetApplicationList,
    getAppLimit: GetAppLimit,
    private val applicationInfoProvider: ApplicationInfoProvider,
) : ViewModel() {

    var query by mutableStateOf("")
        private set

    val appLimitUiState = getApplicationList(UseCase.None).flatMapLatest { appsInfos ->
        snapshotFlow { query }.flatMapLatest { searchQuery ->
            getAppLimit(UseCase.None).mapLatest { appsLimit ->
                appsInfos.filter {
                    applicationInfoProvider.getApplicationName(it.packageName).contains(searchQuery, ignoreCase = true)
                }.map { appInfo ->
                    appsLimit.firstOrNull { it.packageName == appInfo.packageName } ?: AppLimitEntity(
                        id = 0,
                        packageName = appInfo.packageName,
                        limitTimeInMillis = 0,
                        type = AppLimitType.DEFAULT
                    )
                }
            }
        }
    }.map {
        AppLimitUiState.WithData(it)
    }.flowOn(Dispatchers.IO)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = AppLimitUiState.Empty
        )

    fun updateQuery(query: String) {
        this.query = query
    }
}