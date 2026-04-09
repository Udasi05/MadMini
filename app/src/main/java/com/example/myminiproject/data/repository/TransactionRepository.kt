package com.example.myminiproject.data.repository

import com.example.myminiproject.data.model.Transaction
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class TransactionRepository {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private fun getUserId(): String = auth.currentUser?.uid ?: "demo_user"

    private fun collection() = db.collection("users").document(getUserId()).collection("transactions")

    suspend fun addTransaction(transaction: Transaction): Result<String> {
        return try {
            val doc = collection().document()
            val tx = transaction.copy(id = doc.id, userId = getUserId())
            doc.set(tx).await()
            Result.success(doc.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getTransactions(): Result<List<Transaction>> {
        return try {
            val snapshot = collection()
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .await()
            val transactions = snapshot.documents.mapNotNull { doc ->
                doc.toObject(Transaction::class.java)
            }
            Result.success(transactions)
        } catch (e: Exception) {
            // Return sample data if Firestore fails
            Result.success(Transaction.getSampleTransactions())
        }
    }

    suspend fun deleteTransaction(id: String): Result<Unit> {
        return try {
            collection().document(id).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
