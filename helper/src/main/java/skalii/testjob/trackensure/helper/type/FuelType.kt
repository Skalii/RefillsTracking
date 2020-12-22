package skalii.testjob.trackensure.helper.type


import androidx.room.TypeConverter


enum class FuelType(val value: String) {

    EMPTY(""),
    UNKNOWN("Unknown fuel"),
    BENZINE("Benzine"),
    DIESEL("Diesel"),
    GAS("Gas"),
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