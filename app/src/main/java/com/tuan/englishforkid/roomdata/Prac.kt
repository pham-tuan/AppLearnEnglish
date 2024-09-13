package com.tuan.englishforkid.roomdata

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "practice_table")
data class Prac(
    @PrimaryKey(autoGenerate = true) val id:Int? = 0,
    @ColumnInfo(name = "type") val type: String?,
    @ColumnInfo(name = "img") val img: String?,
    @ColumnInfo(name = "vocabulary") val vocabulary: String?,
    @ColumnInfo(name = "spelling") val spelling: String?,
    @ColumnInfo(name = "mean") val mean: String?,
    @ColumnInfo(name = "sound") val sound: String?,
    @ColumnInfo(name = "status") val status: String?,
    @ColumnInfo(name = "note") val note: String?,
    @ColumnInfo(name = "result") val result: String?,
    @ColumnInfo(name = "a") val a: String?,
    @ColumnInfo(name = "b") val b: String?,
    @ColumnInfo(name = "c") val c: String?,
    @ColumnInfo(name = "d") val d: String?,
)
