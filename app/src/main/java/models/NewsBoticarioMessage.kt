package models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class NewsBoticarioMessage(
    @SerializedName("content") val content: String?,
    @SerializedName("created_at") val created_at: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(content)
        parcel.writeString(created_at)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<NewsBoticarioMessage> {
        override fun createFromParcel(parcel: Parcel): NewsBoticarioMessage {
            return NewsBoticarioMessage(parcel)
        }

        override fun newArray(size: Int): Array<NewsBoticarioMessage?> {
            return arrayOfNulls(size)
        }
    }
}