package com.example.myminiproject.data.local.dao

import androidx.room.*
import com.example.myminiproject.data.local.entities.UserProfileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProfileDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(profile: UserProfileEntity)

    @Update
    suspend fun update(profile: UserProfileEntity)

    @Query("SELECT * FROM user_profiles WHERE uid = :uid")
    suspend fun getProfileByUid(uid: String): UserProfileEntity?

    @Query("SELECT * FROM user_profiles WHERE uid = :uid")
    fun observeProfile(uid: String): Flow<UserProfileEntity?>

    @Query("UPDATE user_profiles SET name = :name, state = :state, landHoldingHectares = :landHolding, cropsGrown = :crops, isSynced = 0 WHERE uid = :uid")
    suspend fun updateProfileFields(uid: String, name: String, state: String, landHolding: Double, crops: String)

    @Query("UPDATE user_profiles SET language = :language WHERE uid = :uid")
    suspend fun updateLanguage(uid: String, language: String)

    @Query("UPDATE user_profiles SET notificationsEnabled = :enabled WHERE uid = :uid")
    suspend fun updateNotifications(uid: String, enabled: Boolean)

    @Query("SELECT * FROM user_profiles WHERE isSynced = 0")
    suspend fun getUnsyncedProfiles(): List<UserProfileEntity>

    @Query("UPDATE user_profiles SET isSynced = 1 WHERE uid = :uid")
    suspend fun markAsSynced(uid: String)

    @Query("DELETE FROM user_profiles WHERE uid = :uid")
    suspend fun deleteProfile(uid: String)
}
