package pl.osmalek.bartek.rxbootcamp

import android.util.Log
import android.util.LruCache
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.rxkotlin.toSingle
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

const val BASE_URL = "https://thedroidsonroids.com/wp-json/wp/v2/"

class WordpressService {
    val cache = LruCache<Long, User>(20)
    private val wordpress: Wordpress = Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(Wordpress::class.java)

    fun getPosts(search: CharSequence): Single<List<PostWithAuthor>> {
        return wordpress.getPosts(search.toString())
                .flatMapObservable { Observable.fromIterable(it) }
                .flatMapSingle { post ->
                    getUser(post.author)
                            .map { (name) ->
                                PostWithAuthor(post.slug, name)
                            }
                }
                .toList()
                .timeout(10, TimeUnit.SECONDS)
    }

    private fun getUser(id: Long) = cache[id]?.logCacheHit()?.toSingle()
            ?: wordpress.getUser(id).doOnSuccess { cache.put(id, it) }

    private fun User.logCacheHit(): User = apply { Log.d(WordpressService::class.java.simpleName, "Cache hit: $name") }

}
