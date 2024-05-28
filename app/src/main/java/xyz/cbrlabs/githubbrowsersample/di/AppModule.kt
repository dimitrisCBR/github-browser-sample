package xyz.cbrlabs.githubbrowsersample.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import xyz.cbrlabs.githubbrowsersample.domain.api.ApiResponseConverterFactory
import xyz.cbrlabs.githubbrowsersample.domain.api.GithubService
import xyz.cbrlabs.githubbrowsersample.domain.db.GithubDb
import xyz.cbrlabs.githubbrowsersample.domain.db.RepoDao
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

private const val CACHE_SIZE = (10 * 1024 * 1024).toLong()

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context): Context {
        return context
    }

    @Singleton
    @Provides
    fun provideGithubService(client: OkHttpClient): GithubService {
        return Retrofit.Builder()
            .client(client)
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
//            .addCallAdapterFactory(ApiResponseConverterFactory())
            .build()
            .create(GithubService::class.java)
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(app: Application): OkHttpClient {
        val httpClient = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)

        // Add logging interceptor
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        httpClient.addInterceptor(logging)

        // Add cache
        val httpCacheDirectory = File(app.getCacheDir(), "http-cache")
        val cache = Cache(httpCacheDirectory, CACHE_SIZE)
        httpClient.cache(cache)

        return httpClient.build()
    }

    @Singleton
    @Provides
    fun provideDb(app: Application): GithubDb {
        return Room
            .databaseBuilder(app, GithubDb::class.java, "github.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideRepoDao(db: GithubDb): RepoDao {
        return db.repoDao()
    }
}
