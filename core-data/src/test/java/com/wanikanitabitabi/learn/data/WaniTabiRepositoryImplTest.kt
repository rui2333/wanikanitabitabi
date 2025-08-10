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

package com.wanikanitabitabi.learn.data

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import com.wanikanitabitabi.learn.core.data.WaniTabiRepositoryImpl
import com.wanikanitabitabi.learn.core.database.WaniTabi
import com.wanikanitabitabi.learn.core.database.WaniTabiDao

/**
 * Unit tests for [WaniTabiRepositoryImpl].
 */
@OptIn(ExperimentalCoroutinesApi::class) // TODO: Remove when stable
class WaniTabiRepositoryImplTest {

    @Test
    fun waniTabis_newItemSaved_itemIsReturned() = runTest {
        val repository = WaniTabiRepositoryImpl(FakeWaniTabiDao())

        repository.add("Repository")

        assertEquals(repository.waniTabis.first().size, 1)
    }

}

private class FakeWaniTabiDao : WaniTabiDao {

    private val data = mutableListOf<WaniTabi>()

    override fun getWaniTabis(): Flow<List<WaniTabi>> = flow {
        emit(data)
    }

    override suspend fun insertWaniTabi(item: WaniTabi) {
        data.add(0, item)
    }
}
