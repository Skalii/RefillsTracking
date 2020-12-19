package skalii.testjob.trackensure.data.local.converter


import androidx.room.TypeConverter

import skalii.testjob.trackensure.data.local.converter.base.BaseConverter


class PairDoubleDoubleConverter : BaseConverter<Pair<Double, Double>>() {

    @TypeConverter
    override fun toString(value: Pair<Double, Double>?): String =
        "[${value?.first}°N,${value?.second}°E]"

    @TypeConverter
    override fun fromString(value: String?): Pair<Double, Double>? =
        if (value.isNullOrEmpty()) null else objectFromString(value)

    override fun objectFromString(value: String): Pair<Double, Double> =
        Pair(
            value.substring(1, value.indexOf("°N")).toDouble(),
            value.substring(value.indexOf(",") + 1, value.indexOf("°E")).toDouble()
        )

}