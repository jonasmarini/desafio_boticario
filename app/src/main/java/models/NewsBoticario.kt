package models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class NewsBoticario(
    @SerializedName("user") val user: NewsBoticarioUser?,
    @SerializedName("message") val message: NewsBoticarioMessage?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readParcelable(NewsBoticarioUser::class.java.classLoader),
        parcel.readParcelable(NewsBoticarioMessage::class.java.classLoader)
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(user, flags)
        parcel.writeParcelable(message, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<NewsBoticario> {
        override fun createFromParcel(parcel: Parcel): NewsBoticario {
            return NewsBoticario(parcel)
        }

        override fun newArray(size: Int): Array<NewsBoticario?> {
            return arrayOfNulls(size)
        }
    }
}