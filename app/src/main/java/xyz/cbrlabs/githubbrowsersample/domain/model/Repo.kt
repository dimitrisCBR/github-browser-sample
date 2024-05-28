package xyz.cbrlabs.githubbrowsersample.domain.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import xyz.cbrlabs.githubbrowsersample.domain.api.model.GithubLicense
import xyz.cbrlabs.githubbrowsersample.domain.api.model.GithubOwner
import xyz.cbrlabs.githubbrowsersample.domain.api.model.GithubRepoItem
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
data class Repo(
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
    val license: License?,
    @Embedded(prefix = "owner_")
    val owner: Owner,
    val language: String?,
    val timestamp: Long
) {

    companion object {
        fun fromGithubRepo(repoItem: GithubRepoItem): Repo {
            return Repo(
                repoItem.name,
                repoItem.description,
                repoItem.stargazersCount,
                repoItem.watchersCount,
                repoItem.forksCount,
                repoItem.openIssuesCount,
                repoItem.license?.let { License.fromGithubLicense(it) },
                Owner.fromGithubOwner(repoItem.owner),
                repoItem.language,
                System.currentTimeMillis()
            )
        }
    }

    data class Owner(
        @PrimaryKey(autoGenerate = false)
        val name: String,
        val profileLink: String,
        val avatar: String
    ) {

        companion object {
            fun fromGithubOwner(owner: GithubOwner): Owner {
                return Owner(
                    owner.name,
                    owner.htmlUrl,
                    owner.avatarUrl
                )
            }
        }
    }

    data class License(
        val key: String?,
        val name: String?,
        val spdxId: String?,
        val url: String?,
        val nodeId: String?
    ) : Serializable {
        companion object {
            fun fromGithubLicense(license: GithubLicense): License {
                return License(
                    license.key,
                    license.name,
                    license.spdxId,
                    license.url,
                    license.nodeId
                )
            }
        }
    }
}
