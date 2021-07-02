package utils

import java.text.SimpleDateFormat
import java.util.*

fun String.formatDate(): String{

    val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
    val date = format.parse(this)

    date?.let {
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm")
        return sdf.format(date)
    }
    return ""
}

fun Date.formatDateToString(): String {
    this.let {
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm")
        return sdf.format(this)
    }
    return ""
}