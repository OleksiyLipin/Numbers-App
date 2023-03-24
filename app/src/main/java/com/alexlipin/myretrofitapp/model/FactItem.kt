package com.alexlipin.myretrofitapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "number_table")
data class FactItem(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int? = null,
    var number: String,
    val found: Boolean = false,
    val text: String = "",
    val type: String = ""
)