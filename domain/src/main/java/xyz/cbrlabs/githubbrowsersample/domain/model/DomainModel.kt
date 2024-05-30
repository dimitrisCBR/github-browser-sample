package xyz.cbrlabs.githubbrowsersample.domain.model


import java.io.Serializable

data class Repo(
    val name: String,
    val description: String?,
    val stargazersCount: Int,
    val watchersCount: Int,
    val forksCount: Int,
    val openIssuesCount: Int,
    val license: License?,
    val owner: Owner,
    val language: String?,
    val timestamp: Long
)

data class Owner(
    val name: String,
    val profileLink: String,
    val avatar: String
)

data class License(
    val key: String?,
    val name: String?,
    val spdxId: String?,
    val url: String?,
    val nodeId: String?
) : Serializable
