package br.com.painelb.util

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonQualifier
import com.squareup.moshi.ToJson

@JsonQualifier
internal annotation class IntToBoolean

internal class IntToBooleanAdapter {
    @FromJson
    @IntToBoolean
    fun fromJson(value: Int): Boolean {
        return if(value==1) return true else false
    }

    @ToJson
    fun toJson(@IntToBoolean value: Boolean): Int {
        return  if(value) return  1 else 0
    }
}
