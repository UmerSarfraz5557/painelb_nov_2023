package br.com.painelb.network

import br.com.painelb.util.EMPTY

data class Resource<T>(val status: Status, var data: T?, val message: String?) {
    companion object {
        fun <T> success(data: T?): Resource<T> = Resource(Status.SUCCESS, data, null)
        fun <T> error(msg: String, data: T?): Resource<T> = Resource(Status.ERROR, data, msg)
        fun <T> error(msg: String): Resource<T> = error(msg, null)
        fun <T> error(): Resource<T> = error(String.EMPTY, null)
        fun <T> loading(data: T?): Resource<T> = Resource(Status.LOADING, data, null)
        fun <T> loading(): Resource<T> = Resource(Status.LOADING, null, null)
    }
}
