package vn.start.data.remote

import retrofit2.Response
import retrofit2.http.GET
import vn.start.data.remote.dto.DemoDto

interface APIServices {
    @GET("workouts")
    suspend fun getDemo(): Response<DemoDto>
}