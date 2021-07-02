package entities

import com.orm.SugarRecord
import com.orm.dsl.Table
import java.util.*

@Table
class PostsEntity : SugarRecord {
    var author: String? = null
    var message: String? = null
    var created_date: Date? = null
    var userId: Long? = null

    constructor() {}
    constructor(author: String?, message: String?, created_date: Date?, userId: Long?) : super() {
        this.author = author
        this.message = message
        this.created_date = created_date
        this.userId = userId
    }

}