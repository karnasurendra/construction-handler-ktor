package com.handler.workers.karna.persistance

import com.handler.workers.karna.domain.Attendance
import com.handler.workers.karna.domain.Worker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.litote.kmongo.and
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq
import org.litote.kmongo.gte
import org.litote.kmongo.lt

interface AttendanceRepository {

    suspend fun addUserToAttendance(attendance: Attendance): Attendance

    suspend fun removeUserFromAttendance(attendanceId: String)

    suspend fun isWorkerOwnerIsUser(userId: String): Worker?

    suspend fun getAllWorkersInRange(
        userId: String,
        rangeFrom: Long,
        rangeTo: Long
    ): List<Attendance>

    suspend fun getAttendanceById(attendanceId: String): Attendance?

}

class AttendanceRepositoryImpl(
    private val database: CoroutineDatabase
) : AttendanceRepository {

    private val attendanceCollection = database.getCollection<Attendance>()
    private val workerCollection = database.getCollection<Worker>()

    override suspend fun addUserToAttendance(attendance: Attendance): Attendance = withContext(Dispatchers.IO) {
        try {
            attendanceCollection.insertOne(attendance)
            attendance
        } catch (e: Exception) {
            throw PersistenceException("Failed to update attendance.")
        }
    }

    override suspend fun removeUserFromAttendance(attendanceId: String): Unit = withContext(Dispatchers.IO) {
        try {
            attendanceCollection.deleteOne(
                and(
                    Attendance::id eq attendanceId
                )
            )
        } catch (e: Exception) {
            throw PersistenceException("Failed to update attendance.")
        }
    }

    override suspend fun isWorkerOwnerIsUser(userId: String): Worker? = withContext(Dispatchers.IO) {
        try {
            workerCollection.findOne(Worker::userId eq userId)
        } catch (e: Exception) {
            throw PersistenceException("Failed to find match Worker with User.")
        }

    }

    override suspend fun getAllWorkersInRange(
        userId: String,
        rangeFrom: Long,
        rangeTo: Long
    ): List<Attendance> =
        withContext(Dispatchers.IO) {
            try {
                val query = and(
                    Attendance::userId eq userId,
                    Attendance::attendedDate gte rangeFrom,
                    Attendance::attendedDate lt rangeTo
                )
                attendanceCollection.find(query).toList()
            } catch (e: Exception) {
                throw PersistenceException("Failed to fetch attendance list.")
            }
        }

    override suspend fun getAttendanceById(attendanceId: String): Attendance? = withContext(Dispatchers.IO) {
        try {
            attendanceCollection.findOne(Attendance::id eq attendanceId)
        } catch (e: Exception) {
            throw PersistenceException("Failed to get attendance info.")
        }
    }

}