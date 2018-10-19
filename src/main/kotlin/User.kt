class User {
    var fullName: String? = null
    var uid: String? = null
    var username: String? = null

    constructor() {}

    constructor(fullName: String, uid: String, username: String) {
        this.fullName = fullName
        this.uid = uid
        this.username = username
    }
}