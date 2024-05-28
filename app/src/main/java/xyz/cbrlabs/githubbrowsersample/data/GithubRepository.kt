package xyz.cbrlabs.githubbrowsersample.data

import androidx.room.withTransaction
import kotlinx.coroutines.flow.Flow
import xyz.cbrlabs.githubbrowsersample.domain.api.GithubService
import xyz.cbrlabs.githubbrowsersample.domain.api.model.GithubRepoItem
import xyz.cbrlabs.githubbrowsersample.domain.db.GithubDb
import xyz.cbrlabs.githubbrowsersample.domain.db.RepoDao
import xyz.cbrlabs.githubbrowsersample.domain.model.Repo
import xyz.cbrlabs.githubbrowsersample.ui.Result
import javax.inject.Inject

class GithubRepository @Inject constructor(
    private val roomDb: GithubDb,
    private val repoDao: RepoDao,
    private val githubService: GithubService
) {

    fun searchReposFlow(searchTerm: String): Flow<List<Repo>> {
        return repoDao.searchReposForQuery(searchTerm)
    }

    suspend fun searchGithubRepos(searchTerm: String, page: Int): Result<List<Repo>> {
        try {
            // Get data from RemoteDataSource
            val data = githubService.searchRepos(searchTerm, page)
            val domainData = data.items.map { Repo.fromGithubRepo(it) }
            // Save to local
            roomDb.withTransaction {
                repoDao.insertRepos(domainData)
            }
            return Result.Success(domainData)
        } catch (e: Exception) {
            return Result.Error(e)
        }
    }

    suspend fun loadRepo(owner: String, repoName: String): GithubRepoItem {
        return githubService.getRepo(owner, repoName)
    }
}