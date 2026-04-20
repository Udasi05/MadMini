package com.example.myminiproject.data.local.dao

import androidx.room.*
import com.example.myminiproject.data.local.entities.SchemeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SchemeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(schemes: List<SchemeEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(scheme: SchemeEntity)

    @Query("SELECT * FROM schemes ORDER BY id ASC")
    fun getAllSchemes(): Flow<List<SchemeEntity>>

    @Query("SELECT * FROM schemes ORDER BY id ASC")
    suspend fun getAllSchemesOnce(): List<SchemeEntity>

    @Query("SELECT * FROM schemes WHERE category = :category ORDER BY id ASC")
    fun getSchemesByCategory(category: String): Flow<List<SchemeEntity>>

    @Query("SELECT * FROM schemes WHERE id = :id")
    suspend fun getSchemeById(id: Int): SchemeEntity?

    @Query("SELECT * FROM schemes WHERE name LIKE '%' || :query || '%' OR fullName LIKE '%' || :query || '%'")
    suspend fun searchSchemes(query: String): List<SchemeEntity>

    @Query("DELETE FROM schemes")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) FROM schemes")
    suspend fun getSchemeCount(): Int
}
