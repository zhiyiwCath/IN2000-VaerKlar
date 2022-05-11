package no.uio.g19.klesapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import no.uio.g19.klesapp.data.repository.MainRepository

/**
 * Clears the places table.
 *
 * This viewmodel serves fragments related to the settings screen.
 *
 * Documentation: https://developer.android.com/kotlin/coroutines/coroutines-best-practices
 *
 * @property defaultDispatcherIO Serves a CouroutineDispatcher used as default Coroutine context
 *
 */
class SettingsViewModel : ViewModel() {
    private val defaultDispatcherIO: CoroutineDispatcher = Dispatchers.IO

    /** A function that clears the database.
     *
     * The function is a gateway between View and repository.
     * It is called upon in SettingsFragment
     * The function exists so that the Fragment and repository do not directly communicate
     * A Couroutine is used for thread-safety
     */
    fun clearPLaceDatabase(){
        CoroutineScope(defaultDispatcherIO).launch {
            try {
                MainRepository.clearPLaceDatabase()

            }catch (exception: Exception){
                Log.e("Error: SettingsFrag ","Error in MainRepository.clearPLaceDatabase(): ${exception.message}")
            }
        }
    }
}