package skalii.testjob.trackensure.data.local.converter


import androidx.room.TypeConverter

import java.time.LocalDateTime

import skalii.testjob.trackensure.helper.getDateTimeFormatter
import skalii.testjob.trackensure.data.local.converter.base.BaseConverter


class LocalDateTimeConverter : BaseConverter<LocalDateTime>() {

    @TypeConverter
    override fun toString(value: LocalDateTime?): String? =
        value?.format(getDateTimeFormatter())

    override fun objectFromString(value: String): LocalDateTime? =
        LocalDateTime.parse(value, getDateTimeFormatter())

}