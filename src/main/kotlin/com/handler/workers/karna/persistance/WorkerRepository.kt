package com.handler.workers.karna.persistance

import com.handler.workers.karna.domain.Worker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

interface WorkerRepository {

    suspend fun findWorkerById(workerId: String): Worker?

    suspend fun saveWorker(worker: Worker): Worker

    suspend fun deleteWorker(workerId: String)

    suspend fun getWorkersList(userId: String, page: Int, count: Int): List<Worker>

}

class WorkerRepositoryImpl(private val database: CoroutineDatabase) : WorkerRepository {

    private val workerCollection = database.getCollection<Worker>()

    override suspend fun findWorkerById(workerId: String): Worker? = withContext(Dispatchers.IO) {
        try {
            workerCollection.findOne(Worker::id eq workerId)
        } catch (e: Exception) {
            throw PersistenceException("Failed to fetch worker info.")
        }
    }

    override suspend fun saveWorker(worker: Worker): Worker = withContext(Dispatchers.IO) {
        try {
            workerCollection.insertOne(worker)
            worker
        } catch (e: Exception) {
            throw PersistenceException("Failed to save worker")
        }

    }

    override suspend fun deleteWorker(workerId: String): Unit = withContext(Dispatchers.IO) {
        try {
            workerCollection.deleteOneById(workerId)
        } catch (e: Exception) {
            throw PersistenceException("Failed to delete worker with id $workerId")
        }
    }

    override suspend fun getWorkersList(userId: String, page: Int, count: Int): List<Worker> =
        withContext(Dispatchers.IO) {
            try {
                /**[count+1] helps to check the next page is available or not,  which we can send it in response*/
                /**[count+1] helps to check the next page is available or not,  which we can send it in response*/
                workerCollection.find(Worker::userId eq userId).skip((page - 1) * (count)).limit(count + 1)
                    .toList()
            } catch (e: Exception) {
                throw PersistenceException("Failed to fetch workers list")
            }
        }

}