package com.handler.workers.karna.validations

import com.handler.workers.karna.api.AttendanceDto

class AttendanceValidator {

    fun validateAttendanceDto(attendanceDto: AttendanceDto): List<String> {

        val errorsList = mutableListOf<String>()

        if (attendanceDto.workerName.isEmpty()) {
            errorsList.add("Invalid worker name")
        }

        if (attendanceDto.workerId.isEmpty()) {
            errorsList.add("Invalid worker id")
        }

        if (attendanceDto.attendedDate == 0L) {
            errorsList.add("Invalid date")
        }

        return errorsList

    }

}