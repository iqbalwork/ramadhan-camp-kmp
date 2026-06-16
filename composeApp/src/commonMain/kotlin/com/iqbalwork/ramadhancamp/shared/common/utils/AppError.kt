package com.iqbalwork.ramadhancamp.shared.common.utils

val serverErrorsCodes = 500..599

sealed class AppError(
    override val message: String,
    override val cause: Throwable?
) : Exception(message, cause){

    class NetworkError(
        message: String,
        cause: Throwable
    ) : AppError(message, cause)

    class ServerError(
        val httpCode: Int,
        override val message: String
    ) : AppError(message, null) {
        val isServerError: Boolean
            get() = httpCode in serverErrorsCodes
    }

    class UnexpectedError(
        message: String,
        cause: Throwable?
    ): AppError(message, cause)

}

fun Throwable.toAppError() = (this as? AppError) ?: AppError.UnexpectedError("", this.cause)