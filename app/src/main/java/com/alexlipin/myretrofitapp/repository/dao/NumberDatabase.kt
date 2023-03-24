package com.alexlipin.myretrofitapp.repository.dao

import androidx.room.Database
import androidx.room.RoomDatabase

import com.alexlipin.myretrofitapp.model.FactItem

@Database(
    entities = [FactItem::class],
    version = 1,
    exportSchema = false
)
abstract class NumberDatabase: RoomDatabase() {
    abstract fun dao(): NumberDao
}