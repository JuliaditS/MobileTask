package ac.id.unikom.codelabs.radio.data.source.local

import ac.id.unikom.codelabs.radio.data.model.Program
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ProgramDao {
    @Query("SELECT * FROM program")
    fun getPrograms(): LiveData<List<Program>>

    @Query("SELECT * FROM program WHERE id = :programId")
    fun getProgram(programId: String): LiveData<Program>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(plants: List<Program>)
}