package xyz.cbrlabs.githubbrowsersample.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import xyz.cbrlabs.githubbrowsersample.db.model.RepoEntity

/**
 * Interface for database access on Repo related operations.
 */
@Dao
abstract class RepoEntityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(vararg repoEntities: RepoEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertRepos(repositories: List<RepoEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun createRepoIfNotExists(repoEntity: RepoEntity): Long

    @Query("SELECT * FROM repos WHERE name LIKE '%' || :searchTerm || '%' OR description LIKE '%' || :searchTerm || '%'  ORDER BY timestamp ASC")
    abstract fun searchReposForQuery(searchTerm: String): Flow<List<RepoEntity>>

    @Query("DELETE FROM repos")
    abstract fun clearRepos()

    @Query("DELETE FROM repos WHERE name LIKE '%' || :searchTerm || '%' OR description LIKE '%' || :searchTerm || '%'")
    abstract fun clearReposForQuery(searchTerm: String)

}
