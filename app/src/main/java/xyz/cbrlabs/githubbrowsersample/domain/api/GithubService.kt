package xyz.cbrlabs.githubbrowsersample.domain.api

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import xyz.cbrlabs.githubbrowsersample.domain.api.model.ApiResponse
import xyz.cbrlabs.githubbrowsersample.domain.api.model.GithubOwner
import xyz.cbrlabs.githubbrowsersample.domain.api.model.GithubRepoItem
import xyz.cbrlabs.githubbrowsersample.domain.api.model.RepoSearchResponse

/**
 * REST API access points
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

//    @GET("repos/{owner}/{name}/contributors")
//    suspend fun getContributors(
//        @Path("owner") owner: String,
//        @Path("name") name: String
//    ): ApiResponse<List<Contributor>>

    @GET("search/repositories")
    suspend fun searchRepos(@Query("q") query: String, @Query("page") page: Int): RepoSearchResponse
}
