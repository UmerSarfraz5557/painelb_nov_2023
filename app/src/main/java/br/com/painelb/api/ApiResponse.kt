package br.com.painelb.api

import br.com.painelb.PainelbApplication
import br.com.painelb.R
import com.squareup.moshi.Moshi
import retrofit2.Response
import timber.log.Timber
import java.io.IOException
import java.util.regex.Pattern

sealed class ApiResponse<T> {
    companion object {
        fun <T> create(error: Throwable): ApiErrorResponse<T> {
            Timber.d(error.toString())
            return when (error) {
                is IOException -> ApiErrorResponse(PainelbApplication.context.getString(R.string.error_message_network))
                else -> ApiErrorResponse(error.message ?: "unknown error")
            }
        }

        fun <T> create(response: Response<T>): ApiResponse<T> {
            return when {
                response.isSuccessful -> {
                    val body = response.body()
                    when {
                        body == null || response.code() == 204 -> ApiEmptyResponse()
                        else -> ApiSuccessResponse(
                            body = body,
                            linkHeader = response.headers().get("link")
                        )
                    }
                }
                else -> {
                    var msg = response.errorBody()?.string()
                    val errorMsg =
                        if (msg.isNullOrEmpty()) {
                            response.message()
                        } else {
                            var apiError: ApiError? = null
                            try {
                                val moshi = Moshi.Builder().build()
                                val adapter = moshi.adapter(ApiError::class.java)
                                apiError = adapter.fromJson(msg)
                            } catch (e: Exception) {
                                msg =
                                    PainelbApplication.context.getString(R.string.error_message_network)
                            }
                            when {
                                apiError != null && apiError.message.isNotEmpty() -> apiError.message
                                else -> msg
                            }
                        }
                    ApiErrorResponse(errorMsg ?: "unknown error")
                }
            }
        }
    }

    class ApiResponseImpl<T> : ApiResponse<T>()
}

/**
 * separate class for HTTP 204 responses so that we can make ApiSuccessResponse's body non-null.
 */
class ApiEmptyResponse<T> : ApiResponse<T>()

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
data class ApiSuccessResponse<T>(
    val body: T,
    val links: Map<String, String>
) : ApiResponse<T>() {
    constructor(body: T, linkHeader: String?) : this(
        body = body,
        links = linkHeader?.extractLinks() ?: emptyMap()
    )

    val nextPage: Int? by lazy(LazyThreadSafetyMode.NONE) {
        links[NEXT_LINK]?.let { next ->
            val matcher = PAGE_PATTERN.matcher(next)
            when {
                !matcher.find() || matcher.groupCount() != 1 -> null
                else -> try {
                    Integer.parseInt(matcher.group(1))
                } catch (ex: NumberFormatException) {
                    Timber.w("cannot parse next page from %s", next)
                    null
                }
            }
        }
    }

    companion object {
        private val LINK_PATTERN = Pattern.compile("<([^>]*)>[\\s]*;[\\s]*rel=\"([a-zA-Z0-9]+)\"")
        private val PAGE_PATTERN = Pattern.compile("\\bpage=(\\d+)")
        private const val NEXT_LINK = "next"

        private fun String.extractLinks(): Map<String, String> {
            val links = mutableMapOf<String, String>()
            val matcher = LINK_PATTERN.matcher(this)
            while (matcher.find()) {
                val count = matcher.groupCount()
                if (count == 2) {
                    links[matcher.group(2)] = matcher.group(1)
                }
            }
            return links
        }
    }
}

data class ApiErrorResponse<T>(val errorMessage: String) : ApiResponse<T>()
