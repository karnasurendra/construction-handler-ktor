package com.handler.workers.karna.validations

import com.handler.workers.karna.api.WorkerDto

class WorkerDtoValidator {

    fun validateWorker(workerDto: WorkerDto): List<String> {

        val list = mutableListOf<String>()

        if (workerDto.name.length < 5 || workerDto.name.length > 20) {
            list.add("Name should be greater then 5 and less than 20 characters")
        }

        if (workerDto.mobileNumber.length != 10) {
            list.add("Provide valid  mobile number")
        }

        if (workerDto.dailyWage == 0) {
            list.add("Daily wage should be greater than 0")
        }

        if (workerDto.expertiseIn.isEmpty()){
            list.add("Worker should be specialize in at least one section")
        }


        return list

    }

}