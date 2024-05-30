package xyz.cbrlabs.githubbrowsersample.network

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import xyz.cbrlabs.githubbrowsersample.network.model.GithubOwner
import xyz.cbrlabs.githubbrowsersample.network.model.GithubRepoItem
import xyz.cbrlabs.githubbrowsersample.network.model.RepoSearchResponse

/**
 * Github REST API access points
 */
interface GithubService {
    @GET("users/{login}")
    suspend fun getUser(@Path("login") login: String): GithubOwner

    @GET("users/{login}/repos")
    suspend fun getRepos(@Path("login") login: String): List<GithubRepoItem>

    @GET("repos/{owner}/{name}")
    suspend fun getRepo(
        @Path("owner") owner: String,
        @Path("name") name: String
    ): GithubRepoItem

    @GET("search/repositories")
    suspend fun searchRepos(@Query("q") query: String, @Query("page") page: Int): RepoSearchResponse
}
