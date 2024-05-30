package xyz.cbrlabs.githubbrowsersample.network.di

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import xyz.cbrlabs.githubbrowsersample.network.GithubService
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


private const val CACHE_SIZE = (10 * 1024 * 1024).toLong()

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

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
}