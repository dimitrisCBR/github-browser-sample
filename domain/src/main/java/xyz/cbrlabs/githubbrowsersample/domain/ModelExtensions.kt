package xyz.cbrlabs.githubbrowsersample.domain

import xyz.cbrlabs.githubbrowsersample.db.model.LicenseEntity
import xyz.cbrlabs.githubbrowsersample.db.model.OwnerEntity
import xyz.cbrlabs.githubbrowsersample.db.model.RepoEntity
import xyz.cbrlabs.githubbrowsersample.domain.model.License
import xyz.cbrlabs.githubbrowsersample.domain.model.Owner
import xyz.cbrlabs.githubbrowsersample.domain.model.Repo
import xyz.cbrlabs.githubbrowsersample.network.model.GithubLicense
import xyz.cbrlabs.githubbrowsersample.network.model.GithubOwner
import xyz.cbrlabs.githubbrowsersample.network.model.GithubRepoItem


fun GithubRepoItem.toEntity() = RepoEntity(
    this.name,
    this.description,
    this.stargazersCount,
    this.watchersCount,
    this.forksCount,
    this.openIssuesCount,
    this.license?.toEntity(),
    this.owner.toEntity(),
    this.language,
    System.currentTimeMillis()
)

fun GithubLicense.toEntity() = LicenseEntity(
    this.key,
    this.name,
    this.spdxId,
    this.url,
    this.nodeId
)

fun GithubOwner.toEntity() = OwnerEntity(
    this.name,
    this.url,
    this.avatarUrl,
)

fun RepoEntity.toModel() = Repo(
    this.name,
    this.description,
    this.stargazersCount,
    this.watchersCount,
    this.forksCount,
    this.openIssuesCount,
    this.license?.toModel(),
    this.owner.toModel(),
    this.language,
    System.currentTimeMillis()
)

fun LicenseEntity.toModel() = License(
    this.key,
    this.name,
    this.spdxId,
    this.url,
    this.nodeId
)

fun OwnerEntity.toModel() = Owner(
    this.name,
    this.profileLink,
    this.avatar
)