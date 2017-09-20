package pl.osmalek.bartek.rxbootcamp

import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

const val BASE_URL = "https://thedroidsonroids.com/wp-json/wp/v2/"

class WordpressService {
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
                    getUser(post)
                            .map { (name) ->
                                PostWithAuthor(post.slug, name)
                            }
                }
                .toList()
                .timeout(10, TimeUnit.SECONDS)
    }

    private fun getUser(it: Post) = wordpress.getUser(it.author)
}