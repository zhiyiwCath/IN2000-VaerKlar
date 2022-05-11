package no.uio.g19.klesapp.data

import android.util.Log
import com.google.gson.Gson
import no.uio.g19.klesapp.data.model.wardrobe.Antrekk
import no.uio.g19.klesapp.data.model.wardrobe.Plagg
import no.uio.g19.klesapp.data.model.wardrobe.Wardrobe
import android.content.Context
import java.io.IOException

/**
 * The Database class stores a wardrobe of outfits and clothes, and functions to retrieve these
 *
 * The Database-class contains a Wardrobe dataclass, that again contains a list of
 * Antrekk- and Plagg-objects
 * The Database is created in the View through functions in the MainRepository
 * and HomeViewModel. This is because the Views context is needed to access the
 * JSON-file on which it is based
 *
 * On construction the Database creates itself by reading a locally stored JSON-file
 * The filename is called as an argument, but the file must contain a Wardrobe-object
 *
 *
 * @param context is the context of the View in which it is created
 * @param fileName is the name of the JSON-file the database is created from
 * @constructor the constructor runs the createWardrobe-function
 */

class FileDatabase(context : Context, fileName: String) {

    private lateinit var newWardrobe: Wardrobe

    init {
        createWardrobe(context, fileName)
    }

    /**
     * getJsonFile is used by createWardrobe() to fetch and read a JSON-file
     *
     * @param context is the context in which the file exists
     * @param fileName is the name of the file
     * @return null or a String
     */
    private fun getJsonFile(context: Context, fileName: String): String? {
        val jsonString : String
        try {
            jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return null
        }
        return jsonString
    } //Source: https://bezkoder.com/kotlin-android-read-json-file-assets-gson/

    /**
     * createWardrobe is used by the constructor to create the Wardrobe object
     *
     * The function exists outside of the init in case we want to make it
     * possible to create a database with an empty wardrobe.
     *
     * The function uses getJsonFile to get the contents of the file as a string
     * Then Gson is used to read the file and create a Wardrobe-objects based
     * on its contents.
     *
     * @param context is the context where the JSON-file exists
     * @param fileName is the name of the JSON-file
     */
   private fun createWardrobe(context: Context, fileName: String){
        val gson = Gson()
        try{
            val localFile = getJsonFile(context, fileName)
            if (localFile != null) {
                Log.d("init fil:", localFile)
                newWardrobe = gson.fromJson(localFile, Wardrobe::class.java)
                    Log.d("init wardrobe", newWardrobe.toString())
                    //Log.d("init outfits", newWardrobe.outfits.toString())
                    //Log.d("init clothes", newWardrobe.clothes.toString())

            } else {Log.d("error: ", "localFile = null")}
        } catch (ioException: IOException){
            ioException.printStackTrace()
    }


    }


    /**
     * getOutfits is a getter function that returns all outfits in the wardrobe
     *
     * @return newWardrobe.outfits, a list of Antrekk-objects
     */
    fun getOutfits():List<Antrekk>{
        Log.d("output:", newWardrobe.outfits.toString())
        return newWardrobe.outfits
    }
    /**
     * getClothes is a getter function that returns all clothes in the wardrobe
     *
     * @return newWardrobe.clothes, a list of Plagg-objects
     */
    fun getClothes():List<Plagg>{
            Log.d("output:", newWardrobe.clothes.toString())
            return newWardrobe.clothes
    }
}