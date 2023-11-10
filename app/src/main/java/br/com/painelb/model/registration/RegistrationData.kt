package br.com.painelb.model.registration


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RegistrationData(
    @Json(name = "name")
    val name: String,
    @Json(name = "password")
    val password: String,
    @Json(name = "registration")
    val registration: String,
    @Json(name = "users_id")
    val usersId: Long,
    @Json(name = "cpf")
    val cpf: String,
    @Json(name = "fixed_team")
    val fixedTeam: String,
    @Json(name = "variable_team")
    val variableTeam: String,
    @Json(name = "sector")
    val sector: String
)