package skalii.testjob.trackensure.data.local


import android.content.Context

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

import kotlinx.serialization.ExperimentalSerializationApi

import skalii.testjob.trackensure.data.local.converter.*
import skalii.testjob.trackensure.data.local.dao.*
import skalii.testjob.trackensure.helper.model.*
import skalii.testjob.trackensure.helper.type.*


@Database(
    entities = [GasStation::class, Refill::class, Supplier::class],
    version = 1,
    exportSchema = false
)
@ExperimentalSerializationApi
@TypeConverters(
    LocalDateTimeConverter::class, PairDoubleDoubleConverter::class,
    FuelType::class
)
abstract class TrackDatabase : RoomDatabase() {

    abstract fun getGasStationDao(): GasStationDao
    abstract fun getRefillDao(): RefillDao
    abstract fun getSupplierDao(): SupplierDao

    companion object {

        @Volatile
        private var INSTANCE: TrackDatabase? = null

        fun getInstance(context: Context): TrackDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context,
                TrackDatabase::class.java,
                "refills_tracking.db"
            )
                .setJournalMode(JournalMode.TRUNCATE)
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries() //todo check this on release build
                /*.addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                    }
                })*/
                .build()
    }

}