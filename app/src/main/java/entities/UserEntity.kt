package entities

import com.orm.SugarRecord
import com.orm.dsl.Table

@Table
class UserEntity : SugarRecord {
    var name: String? = null
    var email: String? = null
    var password: String? = null

    constructor() {}
    constructor(name: String?, email: String?, password: String?) : super() {
        this.name = name
        this.email = email
        this.password = password
    }

}