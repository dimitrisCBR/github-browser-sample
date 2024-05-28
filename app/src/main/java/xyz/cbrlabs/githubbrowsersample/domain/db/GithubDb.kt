package xyz.cbrlabs.githubbrowsersample.domain.db

import androidx.room.Database
import androidx.room.RoomDatabase
import xyz.cbrlabs.githubbrowsersample.domain.model.Repo

@Database(
    entities = [Repo::class],
    version = 4,
    exportSchema = false
)
abstract class GithubDb : RoomDatabase() {

    abstract fun repoDao(): RepoDao
}
