package com.handler.workers.karna.api

import com.handler.workers.karna.domain.Attendance
import kotlinx.serialization.Serializable

@Serializable
data class AttendanceDto(
    val workerName: String = String(),
    val workerId: String = String(),
    val userId: String = String(),
    val attendedDate: Long
)

fun AttendanceDto.fromDto(): Attendance {
    return Attendance(
        workerName = this.workerName,
        workerId = this.workerId,
        userId = this.userId,
        attendedDate = this.attendedDate
    )
}

@Serializable
data class AttendanceOverViewDto(
    val id: String = String(),
    val workerName: String = String(),
    val workerId: String = String(),
    val userId: String = String(),
    val attendedDate: Long
)

fun Attendance.toDto(): AttendanceOverViewDto {
    return AttendanceOverViewDto(
        id = this.id,
        workerId = this.workerId,
        workerName = this.workerName,
        attendedDate = this.attendedDate
    )
}

@Serializable
data class AttendanceListDto(
    val attendanceList: List<AttendanceOverViewDto>
)

fun List<Attendance>.toAttendanceDtoList(): List<AttendanceOverViewDto> {
    return this.map { it.toDto() }
}
