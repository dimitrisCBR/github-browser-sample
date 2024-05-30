package xyz.cbrlabs.githubbrowsersample.network.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class RepoSearchResponse(
    @SerializedName("total_count") val total: Int = 0,
    @SerializedName("incomplete_results") val incomplete: Boolean = false,
    @SerializedName("items") val items: List<GithubRepoItem>
)

data class GithubRepoItem(
    @SerializedName("id") val id: Long,
    @SerializedName("node_id") val nodeId: String,
    @SerializedName("name") val name: String,
    @SerializedName("full_name") val fullName: String,
    @SerializedName("private") val private: Boolean = false,
    @SerializedName("owner") val owner: GithubOwner,
    @SerializedName("html_url") val htmlUrl: String,
    @SerializedName("description") val description: String = "",
    @SerializedName("fork") val fork: Boolean = false,
    @SerializedName("url") val url: String,
    @SerializedName("homepage") val homepage: String? = null,
    @SerializedName("size") val size: Int? = null,
    @SerializedName("stargazers_count") val stargazersCount: Int = 0,
    @SerializedName("watchers_count") val watchersCount: Int = 0,
    @SerializedName("language") val language: String? = null,
    @SerializedName("has_issues") val hasIssues: Boolean = false,
    @SerializedName("has_projects") val hasProjects: Boolean = false,
    @SerializedName("has_downloads") val hasDownloads: Boolean = false,
    @SerializedName("has_wiki") val hasWiki: Boolean = false,
    @SerializedName("has_pages") val hasPages: Boolean = false,
    @SerializedName("has_discussions") val hasDiscussions: Boolean = false,
    @SerializedName("forks_count") val forksCount: Int = 0,
    @SerializedName("archived") val archived: Boolean = false,
    @SerializedName("disabled") val disabled: Boolean = false,
    @SerializedName("open_issues_count") val openIssuesCount: Int = 0,
    @SerializedName("license") val license: GithubLicense?,
    @SerializedName("allow_forking") val allowForking: Boolean = false,
    @SerializedName("topics") val topics: ArrayList<String> = arrayListOf(),
    @SerializedName("visibility") val visibility: String = "",
    @SerializedName("forks") val forks: Int = 0,
    @SerializedName("open_issues") val openIssues: Int = 0,
    @SerializedName("watchers") val watchers: Int = 0,
    @SerializedName("default_branch") val defaultBranch: String,
    @SerializedName("score") val score: Int = 0
) : Serializable

data class GithubOwner(
    @SerializedName("login") val name: String,
    @SerializedName("id") val id: Long,
    @SerializedName("node_id") val nodeId: String,
    @SerializedName("avatar_url") val avatarUrl: String,
    @SerializedName("url") val url: String,
    @SerializedName("html_url") val htmlUrl: String,
    @SerializedName("followers_url") val followersUrl: String? = null,
    @SerializedName("following_url") val followingUrl: String? = null,
    @SerializedName("gists_url") val gistsUrl: String? = null,
    @SerializedName("starred_url") val starredUrl: String? = null,
    @SerializedName("subscriptions_url") val subscriptionsUrl: String? = null,
    @SerializedName("organizations_url") val organizationsUrl: String? = null,
    @SerializedName("repos_url") val reposUrl: String? = null,
    @SerializedName("events_url") val eventsUrl: String? = null,
    @SerializedName("received_events_url") val receivedEventsUrl: String? = null,
    @SerializedName("type") val type: String? = null,
    @SerializedName("site_admin") val siteAdmin: Boolean? = null
) : Serializable

data class GithubLicense(
    @SerializedName("key") val key: String,
    @SerializedName("name") val name: String,
    @SerializedName("spdx_id") val spdxId: String,
    @SerializedName("url") val url: String,
    @SerializedName("node_id") val nodeId: String
) : Serializable