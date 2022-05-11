package no.uio.g19.klesapp.data.model.placesDB


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import no.uio.g19.klesapp.data.model.placesDB.entities.PlaceRoomEntity

@Database(
    entities = [PlaceRoomEntity:: class],
    version = 2,
)

/**
 * Specifications for the room-database
 *
 * Based upon the following sources:
 *
 * Source: https://developer.android.com/training/data-storage/room
 * Source: https://www.youtube.com/watch?v=iTdzBM1zErA&t=596s
 * Source: https://proandroiddev.com/synchronization-and-thread-safety-techniques-in-java-and-kotlin-f63506370e6d
 */
abstract class PlacesDatabase: RoomDatabase() {

    abstract val placesDao: PlacesDao

    /**
     * An companion object (singleton) that holds the acutal database in INSTANCE.
     *
     * '@Volatile' ensures that values read from memomry not cpu-cache
     * @property INSTANCE The actual database object
     */
    companion object{
        @Volatile
        private var INSTANCE: PlacesDatabase? = null

        /**
         * A function that sets up the database
         * It gets en instance of the database builder in a thread safe way,
         * by locking value until code block is executed
         * It will recreate the database schema by destroying older if no migrations found
         *
         * @param context An application context giving the Room-database access to the environment
         * @return a RoomDatabaseBuilder<T> that can be used to create the database
         */
        fun getInstance(context: Context): PlacesDatabase {
            synchronized(this) {
                return INSTANCE?: Room.databaseBuilder(
                    context.applicationContext,
                    PlacesDatabase::class.java,
                    "places_db"
                ).fallbackToDestructiveMigration().build().also{
                    INSTANCE = it
                }
            }
        }
    }

}