package com.amalcodes.wisescreen.features.screentime.applimit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amalcodes.wisescreen.core.ApplicationInfoProvider
import com.amalcodes.wisescreen.domain.entity.AppLimitEntity
import com.amalcodes.wisescreen.domain.usecase.GetApplicationList
import com.amalcodes.wisescreen.domain.usecase.UseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
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
    private val applicationInfoProvider: ApplicationInfoProvider,
) : ViewModel() {

    var query by mutableStateOf("")
        private set

    private var cachedAppLimit: List<AppLimitEntity> = emptyList()

    val appLimitUiState = snapshotFlow { query }.flatMapLatest { snapshotQuery ->
        if (cachedAppLimit.isNotEmpty()) return@flatMapLatest flowOf(cachedAppLimit).filterByQuery(snapshotQuery)
        getApplicationList(UseCase.None).onEach { cachedAppLimit = it }.filterByQuery(snapshotQuery)
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

    private fun Flow<List<AppLimitEntity>>.filterByQuery(searchQuery: String) = map { list ->
        if (searchQuery.isBlank()) list
        else list.filter { applicationInfoProvider.getApplicationName(it.packageName).contains(searchQuery, ignoreCase = true) }
    }
}