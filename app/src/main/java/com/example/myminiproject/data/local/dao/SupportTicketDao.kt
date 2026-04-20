package com.example.myminiproject.data.local.dao

import androidx.room.*
import com.example.myminiproject.data.local.entities.SupportTicketEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SupportTicketDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(ticket: SupportTicketEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(tickets: List<SupportTicketEntity>)

    @Update
    suspend fun update(ticket: SupportTicketEntity)

    @Query("SELECT * FROM support_tickets WHERE userId = :userId ORDER BY createdAt DESC")
    fun getTicketsByUser(userId: String): Flow<List<SupportTicketEntity>>

    @Query("SELECT * FROM support_tickets WHERE userId = :userId ORDER BY createdAt DESC")
    suspend fun getTicketsByUserOnce(userId: String): List<SupportTicketEntity>

    @Query("SELECT * FROM support_tickets WHERE id = :id")
    suspend fun getTicketById(id: String): SupportTicketEntity?

    @Query("SELECT * FROM support_tickets WHERE isSynced = 0")
    suspend fun getUnsyncedTickets(): List<SupportTicketEntity>

    @Query("UPDATE support_tickets SET isSynced = 1 WHERE id = :id")
    suspend fun markAsSynced(id: String)

    @Query("DELETE FROM support_tickets WHERE userId = :userId")
    suspend fun deleteAllByUser(userId: String)
}
