package com.handler.workers.karna.plugins

import com.handler.workers.karna.api.WorkerController
import com.handler.workers.karna.utils.Constants
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Routing.configureWorkerRouting() {

    val workerController by inject<WorkerController>()

    route(Constants.ApiNames.WORKERS_BASE) {
        authenticate {
            post(Constants.ApiNames.ADD_WORKER) {
                workerController.addWorker(call)
            }

            get(Constants.ApiNames.GET_SINGLE_WORKER_INFO) {
                workerController.getWorkerInfo(call)
            }

            get(Constants.ApiNames.GET_WORKERS_LIST) {
                workerController.getWorkersList(call)
            }

            delete(Constants.ApiNames.DELETE_WORKER) {
                workerController.deleteWorker(call)
            }

        }

    }

}