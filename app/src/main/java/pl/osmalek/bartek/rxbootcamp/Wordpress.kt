package pl.osmalek.bartek.rxbootcamp

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface Wordpress {
    @GET("users/{id}")
    fun getUser(@Path("id") id: Long): Single<User>

    @GET("posts")
    fun getPosts(@Query("search") search: String): Single<List<Post>>
}

