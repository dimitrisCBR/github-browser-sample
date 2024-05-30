package xyz.cbrlabs.githubbrowsersample.db.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Using name/owner_login as primary key instead of id since name/owner_login is always available
 * vs id is not.
 */
@Entity(
    tableName = "repos",
    indices = [Index("name")],
    primaryKeys = ["name"]
)
data class RepoEntity(
    val name: String,
    val description: String?,
    @field:SerializedName("stargazers_count")
    val stargazersCount: Int,
    @field:SerializedName("watchers_count")
    val watchersCount: Int,
    @field:SerializedName("forks_count")
    val forksCount: Int,
    @field:SerializedName("open_issues_count")
    val openIssuesCount: Int,
    @Embedded(prefix = "license_")
    val license: LicenseEntity?,
    @Embedded(prefix = "owner_")
    val owner: OwnerEntity,
    val language: String?,
    val timestamp: Long
)

data class OwnerEntity(
    val name: String,
    val profileLink: String,
    val avatar: String
)

data class LicenseEntity(
    val key: String?,
    val name: String?,
    val spdxId: String?,
    val url: String?,
    val nodeId: String?
) : Serializable
