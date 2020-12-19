package skalii.testjob.trackensure.helper.type


import androidx.room.TypeConverter

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
enum class FuelType(val value: String) {

    @SerialName("")
    EMPTY(""),

    @SerialName("Unknown fuel")
    UNKNOWN("Unknown fuel"),

    @SerialName("Benzine")
    BENZINE("Benzine"),

    @SerialName("Diesel")
    DIESEL("Diesel"),

    @SerialName("Gas")
    GAS("Gas"),

    @SerialName("Kerosene")
    KEROSENE("Kerosene");

    companion object {

        @TypeConverter
        @JvmStatic
        fun toString(enum: FuelType?) = enum?.value ?: UNKNOWN.value

        @TypeConverter
        @JvmStatic
        fun toEnum(data: String?) = values().find { it.value == data } ?: UNKNOWN

    }

}