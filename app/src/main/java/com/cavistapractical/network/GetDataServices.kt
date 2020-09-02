package com.cavistapractical.network

import com.google.gson.JsonObject
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.*

interface GetDataServices {

    @GET("3/gallery/search/{page}?")
    fun search(@Path("page") page: Int, @Query("q") q: String): Observable<Response<JsonObject>>
}