package com.example.tantrataassignment.api

import com.example.tantrataassignment.model.GalleryResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ImgurAPI {

    @Headers("Authorization: Client-ID 652c84888011102")
    @GET("3/gallery/{section}/{sort}/{window}/{page}")
    suspend fun getGalleryData(
        @Path("section") section: String,
        @Path("sort") sort: String,
        @Path("window") window: String,
        @Path("page") page: Int,
        @Query("showViral") showViral: Boolean,
        @Query("mature") mature: Boolean,
        @Query("album_previews") album_previews: Boolean
    ): GalleryResponse

    @Headers("Authorization: Client-ID 652c84888011102")
    @GET("3/gallery/search/{sort}/{window}/{page}")
    suspend fun getGalleryDataBySearch(
        @Path("sort") sort: String,
        @Path("window") window: String,
        @Path("page") page: Int,
        @Query("q") searchText: String,
    ): GalleryResponse
}