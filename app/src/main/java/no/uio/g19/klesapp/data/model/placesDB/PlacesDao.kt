package no.uio.g19.klesapp.data.model.placesDB


import androidx.room.*
import no.uio.g19.klesapp.data.model.placesDB.entities.PlaceRoomEntity

/**
 * Specifications for the Room-database
 *
 * Based upon:
 * Source: https://developer.android.com/training/data-storage/room
 */
@Dao
interface PlacesDao{

   /**
    * Query that fethches the table of PlaceRoomEntities
    *
    * @return List<PlaceRoomEntity>
    */
    @Query("SELECT * FROM Places")
    suspend fun getAllList(): List<PlaceRoomEntity>

    //TODO
    //Delete commented section if project still runs
   /* @Query("SELECT * FROM Places WHERE name = :nameId")
    suspend fun getByName(nameId: String) : PlaceRoomEntity

    @Query("DELETE FROM Places WHERE name = :nameId")
    suspend fun deleteByName(nameId: String)

    @Delete
    suspend fun delete(place: PlaceRoomEntity) */

    //Query that inserts a PlaceRoomEntity into the table.
    //If there is a conflict between two entities, the former one is overwritten.
    /**
     * Query that inserts a variable amount of PlaceRoomEntities into the table
     *
     * If there is a conflict between two entities/inserts, the old entity is overwritten
     *
     * @param place an PlaceRoomEntity constructed in SearchNewLoctionFragment
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg place: PlaceRoomEntity)

    /**
     * Query that clears the table.
     */
    //Query that clears the table.
    @Query("DELETE FROM Places")
    suspend fun clearTable()
}