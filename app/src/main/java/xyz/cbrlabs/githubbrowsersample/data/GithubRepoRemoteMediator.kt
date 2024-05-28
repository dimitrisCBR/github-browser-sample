package xyz.cbrlabs.githubbrowsersample.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import kotlinx.coroutines.flow.last
import xyz.cbrlabs.githubbrowsersample.domain.api.GithubService
import xyz.cbrlabs.githubbrowsersample.domain.db.GithubDb
import xyz.cbrlabs.githubbrowsersample.domain.model.Repo
import xyz.cbrlabs.githubbrowsersample.domain.prefs.DataStoreHelper
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Example implementation of RemoteMediator to use with AndroidX Paging 3 library.
 * Due to the nature of the app screen (it's a search rather than a feed), I opted
 * for manually implementing paging.
 */
@OptIn(ExperimentalPagingApi::class)
class GithubRepoRemoteMediator constructor(
    private val query: String,
    private val database: GithubDb,
    private val networkService: GithubService,
    private val dataStoreHelper: DataStoreHelper,
) : RemoteMediator<Int, Repo>() {
    val dao = database.repoDao()

    private var totalCount: Int = 0
    private var currentPage: Int = 0

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Repo>
    ): MediatorResult {
        return try {
            when (loadType) {
                LoadType.REFRESH -> {
                    totalCount = 0
                    currentPage = 0
                }

                LoadType.PREPEND ->
                    // No reason to prepend items for this app.
                    return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    // If last item is null, there are no more items
                    state.lastItemOrNull() ?: return MediatorResult.Success(
                        endOfPaginationReached = true
                    )
                }
            }

            val response = networkService.searchRepos(
                query = query, page = currentPage
            )

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    dao.clearReposForQuery(query)
                }
                dao.insertRepos(response.items.map { Repo.fromGithubRepo(it) })
            }
            dataStoreHelper.saveLong(DataStoreHelper.KEY_LAST_UPDATED, System.currentTimeMillis())

            totalCount += response.items.size
            currentPage += 1
            MediatorResult.Success(
                endOfPaginationReached = response.total < totalCount
            )
        } catch (t: Throwable) {
            MediatorResult.Error(t)
        }
    }

    override suspend fun initialize(): InitializeAction {
        val cacheTimeout = TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS)
        val lastUpdated = dataStoreHelper.getLong(DataStoreHelper.KEY_LAST_UPDATED).last() ?: 0L
        return if (System.currentTimeMillis().minus(lastUpdated) <= cacheTimeout) {
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }

}
