package com.tuan.englishforkid.roomdata

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_table")
data class Favor(
    @PrimaryKey(autoGenerate = true) val id:Int? = 0,
    @ColumnInfo(name = "img") val img: String?,
    @ColumnInfo(name = "vocabulary") val vocabulary: String?,
    @ColumnInfo(name = "spelling") val spelling: String?,
    @ColumnInfo(name = "mean") val mean: String?,
    @ColumnInfo(name = "sound") val sound: String?,
    @ColumnInfo(name = "status") val status: String?,
    @ColumnInfo(name = "note") val note: String?
)
