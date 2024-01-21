package com.handler.workers.karna.plugins

import com.handler.workers.karna.api.AttendanceController
import com.handler.workers.karna.utils.Constants
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Routing.configureAttendanceRouting() {

    val attendanceController by inject<AttendanceController>()

    route(Constants.ApiNames.ATTENDANCE_BASE) {
        authenticate {
            post(Constants.ApiNames.UPDATE_ATTENDANCE) {
                attendanceController.updateAttendance(call)
            }

            get(Constants.ApiNames.GET_ATTENDANCE) {
                attendanceController.getAttendance(call)
            }
        }
    }
}