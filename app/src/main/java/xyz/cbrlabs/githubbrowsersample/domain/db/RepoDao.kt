package xyz.cbrlabs.githubbrowsersample.domain.db

import androidx.annotation.OpenForTesting
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import xyz.cbrlabs.githubbrowsersample.domain.model.Repo

/**
 * Interface for database access on Repo related operations.
 */
@Dao
@OpenForTesting
abstract class RepoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(vararg repos: Repo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertRepos(repositories: List<Repo>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun createRepoIfNotExists(repo: Repo): Long

    @Query("SELECT * FROM repos WHERE name LIKE '%' || :searchTerm || '%' OR description LIKE '%' || :searchTerm || '%'  ORDER BY timestamp ASC")
    abstract fun searchReposForQuery(searchTerm: String): Flow<List<Repo>>

    @Query("SELECT * FROM repos WHERE name LIKE '%' || :searchTerm || '%' OR description LIKE '%' || :searchTerm || '%'  ORDER BY timestamp ASC")
    abstract fun searchReposForQueryPaging(searchTerm: String): PagingSource<Int, Repo>

    @Query("DELETE FROM repos")
    abstract fun clearRepos()

    @Query("DELETE FROM repos WHERE name LIKE '%' || :searchTerm || '%' OR description LIKE '%' || :searchTerm || '%'")
    abstract fun clearReposForQuery(searchTerm: String)

}
