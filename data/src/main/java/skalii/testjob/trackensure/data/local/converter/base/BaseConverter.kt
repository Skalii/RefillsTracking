package skalii.testjob.trackensure.data.local.converter.base


import androidx.room.TypeConverter


abstract class BaseConverter<Type> {

    @TypeConverter
    open fun toString(value: Type?): String? = value?.toString()

    @TypeConverter
    open fun fromString(value: String?): Type? =
        if (value.isNullOrEmpty()) null else objectFromString(value)

    abstract fun objectFromString(value: String): Type?

}