package skalii.testjob.trackensure.data.type


import androidx.room.TypeConverter

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
enum class FuelType(val value: String) {


    @SerialName("")
    EMPTY(""),

    @SerialName("Unknown fuel")
    UNKNOWN("Unknown fuel"),

    @SerialName("benzine")
    BENZINE("benzine"),

    @SerialName("Diesel")
    DIESEL("Diesel"),

    @SerialName("gas")
    GAS("gas"),

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