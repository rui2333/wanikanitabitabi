/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wanikanitabitabi.learn.feature.wanitabi.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import com.wanikanitabitabi.learn.core.data.WaniTabiRepository
import com.wanikanitabitabi.learn.feature.wanitabi.ui.WaniTabiUiState.Error
import com.wanikanitabitabi.learn.feature.wanitabi.ui.WaniTabiUiState.Loading
import com.wanikanitabitabi.learn.feature.wanitabi.ui.WaniTabiUiState.Success
import javax.inject.Inject

@HiltViewModel
class WaniTabiViewModel @Inject constructor(
    private val waniTabiRepository: WaniTabiRepository
) : ViewModel() {

    val uiState: StateFlow<WaniTabiUiState> = waniTabiRepository
        .waniTabis.map<List<String>, WaniTabiUiState> { Success(data = it) }
        .catch { emit(Error(it)) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Loading)

    fun addWaniTabi(name: String) {
        viewModelScope.launch {
            waniTabiRepository.add(name)
        }
    }
}

sealed interface WaniTabiUiState {
    object Loading : WaniTabiUiState
    data class Error(val throwable: Throwable) : WaniTabiUiState
    data class Success(val data: List<String>) : WaniTabiUiState
}
