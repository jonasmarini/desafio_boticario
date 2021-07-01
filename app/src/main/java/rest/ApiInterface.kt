package rest

import constants.Constants
import models.NewsBoticarioResponse
import retrofit2.Call
import retrofit2.http.GET


interface ApiInterface {
    @GET(Constants.URL_GET_NEWS)
    fun getNewsBoticario(): Call<NewsBoticarioResponse>
}
