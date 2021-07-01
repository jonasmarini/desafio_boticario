package models

import com.google.gson.annotations.SerializedName

data class NewsBoticarioResponse(
    @SerializedName("news") val news: ArrayList<NewsBoticario>
)