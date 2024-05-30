package xyz.cbrlabs.githubbrowsersample.domain.data

import androidx.room.withTransaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import xyz.cbrlabs.githubbrowsersample.commons.Result
import xyz.cbrlabs.githubbrowsersample.db.GithubDb
import xyz.cbrlabs.githubbrowsersample.db.model.RepoEntity
import xyz.cbrlabs.githubbrowsersample.db.RepoEntityDao
import xyz.cbrlabs.githubbrowsersample.domain.model.Repo
import xyz.cbrlabs.githubbrowsersample.domain.toEntity
import xyz.cbrlabs.githubbrowsersample.domain.toModel
import xyz.cbrlabs.githubbrowsersample.network.GithubService
import javax.inject.Inject

class GithubRepository @Inject constructor(
    private val roomDb: GithubDb,
    private val repoEntityDao: RepoEntityDao,
    private val githubService: GithubService
) {

    fun searchReposFlow(searchTerm: String): Flow<List<Repo>> {
        return repoEntityDao.searchReposForQuery(searchTerm)
            .map { items -> items.map { it.toModel() } }
    }

    suspend fun searchGithubRepos(searchTerm: String, page: Int): Result<List<RepoEntity>> {
        try {
            // Get data from RemoteDataSource
            val data = githubService.searchRepos(searchTerm, page)
            val domainData = data.items.map { it.toEntity() }
            // Save to local
            roomDb.withTransaction {
                repoEntityDao.insertRepos(domainData)
            }
            return Result.Success(domainData)
        } catch (e: Exception) {
            return Result.Error(e)
        }
    }

    suspend fun loadRepo(owner: String, repoName: String): Result<Repo> {
        return try {
            Result.Success(githubService.getRepo(owner, repoName).toEntity().toModel())
        } catch (t: Throwable) {
            Result.Error(t)
        }
    }
}