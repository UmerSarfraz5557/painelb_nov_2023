package br.com.painelb.model

import com.squareup.moshi.Json

data class UpdateResponse(
    @Json(name = "update")
    val update : Boolean)