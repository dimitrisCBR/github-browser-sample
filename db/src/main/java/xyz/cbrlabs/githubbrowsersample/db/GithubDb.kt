package xyz.cbrlabs.githubbrowsersample.db

import androidx.room.Database
import androidx.room.RoomDatabase
import xyz.cbrlabs.githubbrowsersample.db.model.RepoEntity

@Database(
    entities = [RepoEntity::class],
    version = 4,
    exportSchema = false
)
abstract class GithubDb : RoomDatabase() {

    abstract fun repoDao(): RepoEntityDao
}
