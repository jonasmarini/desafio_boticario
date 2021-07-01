package models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class NewsBoticarioUser(
    @SerializedName("name") val name: String?,
    @SerializedName("profile_picture") val profile_picture: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(profile_picture)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<NewsBoticarioUser> {
        override fun createFromParcel(parcel: Parcel): NewsBoticarioUser {
            return NewsBoticarioUser(parcel)
        }

        override fun newArray(size: Int): Array<NewsBoticarioUser?> {
            return arrayOfNulls(size)
        }
    }
}