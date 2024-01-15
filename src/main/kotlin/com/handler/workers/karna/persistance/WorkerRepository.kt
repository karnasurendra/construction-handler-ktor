package com.handler.workers.karna.persistance

import com.handler.workers.karna.domain.Worker
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

    override suspend fun findWorkerById(workerId: String): Worker? {
        try {
            return workerCollection.findOne(Worker::id eq workerId)
        } catch (e: Exception) {
            throw PersistenceException("Failed to fetch worker info.")
        }
    }

    override suspend fun saveWorker(worker: Worker): Worker {
        try {
            workerCollection.insertOne(worker)
            return worker
        } catch (e: Exception) {
            throw PersistenceException("Failed to save worker")
        }

    }

    override suspend fun deleteWorker(workerId: String) {
        try {
            workerCollection.deleteOneById(workerId)
        } catch (e: Exception) {
            throw PersistenceException("Failed to delete worker with id $workerId")
        }
    }

    override suspend fun getWorkersList(userId: String, page: Int, count: Int): List<Worker> {
        try {
            /**[count+1] helps to check the next page is available or not,  which we can send it in response*/
            return workerCollection.find(Worker::userId eq userId).skip((page - 1) * (count)).limit(count + 1).toList()
        } catch (e: Exception) {
            throw PersistenceException("Failed to fetch workers list")
        }
    }

}