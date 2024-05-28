package xyz.cbrlabs.githubbrowsersample.domain.usecase

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import xyz.cbrlabs.githubbrowsersample.data.GithubRepoRemoteMediator
import xyz.cbrlabs.githubbrowsersample.data.GithubRepository
import xyz.cbrlabs.githubbrowsersample.domain.api.GithubService
import xyz.cbrlabs.githubbrowsersample.domain.db.GithubDb
import xyz.cbrlabs.githubbrowsersample.domain.db.RepoDao
import xyz.cbrlabs.githubbrowsersample.domain.model.Repo
import xyz.cbrlabs.githubbrowsersample.domain.prefs.DataStoreHelper
import javax.inject.Inject

class GithubReposSearchPagingUseCase @Inject constructor(
    private val repoDao: RepoDao,
    private val database: GithubDb,
    private val networkService: GithubService,
    private val dataStoreHelper: DataStoreHelper,
) {

    @OptIn(ExperimentalPagingApi::class)
    fun call(searchTerm: String): Flow<PagingData<Repo>> = Pager(
        config = PagingConfig(20),
        remoteMediator = GithubRepoRemoteMediator(
            searchTerm,
            database,
            networkService,
            dataStoreHelper
        )
    ) {
        repoDao.searchReposForQueryPaging(searchTerm)
    }.flow
}