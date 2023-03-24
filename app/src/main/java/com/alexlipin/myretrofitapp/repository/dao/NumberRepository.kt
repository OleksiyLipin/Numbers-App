package com.alexlipin.myretrofitapp.repository.dao

import androidx.annotation.WorkerThread

import com.alexlipin.myretrofitapp.model.FactItem
import kotlinx.coroutines.flow.Flow

class NumberRepository (private val dao: NumberDao) {

    val allItems: Flow<List<FactItem>> = dao.getItems()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(item: FactItem) {
        dao.insertItem(item)
    }
}