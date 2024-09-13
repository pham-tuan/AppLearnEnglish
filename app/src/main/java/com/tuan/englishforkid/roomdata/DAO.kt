package com.tuan.englishforkid.roomdata

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface DAO {

    //favorite
    @Query("SELECT * FROM favorite_table ")
    fun getAll(): LiveData<List<Favor>>

    @Query("SELECT * FROM favorite_table ORDER BY RANDOM()")
    fun getRandomFavorites(): LiveData<List<Favor>>

    @Delete
    suspend fun delete(favor: Favor)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertfavor(favor: Favor)

    @Query("DELETE FROM favorite_table WHERE vocabulary = :vocabulary")
    suspend fun deleteByVoca(vocabulary: String): Int

    @Query("SELECT COUNT(*) FROM favorite_table WHERE vocabulary = :vocabulary")
    suspend fun existsFavor(vocabulary: String): Int
    // practice

    @Query("SELECT * FROM practice_table ")
    fun getAllPrac(): LiveData<List<Prac>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(prac: Prac): Long

    @Query("SELECT * FROM practice_table ORDER BY RANDOM()")
    fun getRandomPractice(): LiveData<List<Prac>>

    @Query("SELECT COUNT(*) FROM practice_table WHERE vocabulary = :vocabulary")
    suspend fun exists(vocabulary: String): Int

}