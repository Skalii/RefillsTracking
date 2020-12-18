package skalii.testjob.trackensure.data

import java.time.format.DateTimeFormatter


fun getDateTimeFormatter(): DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")