package br.com.painelb.model.login


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginResponse(
    @Json(name = "email")
    val email: String,
    @Json(name = "message")
    val message: String,
    @Json(name = "name")
    val name: String,
    @Json(name = "token")
    val token: String,
    @Json(name = "users_id")
    val usersId: Long,
    @Json (name = "level")
    val level : Int
)