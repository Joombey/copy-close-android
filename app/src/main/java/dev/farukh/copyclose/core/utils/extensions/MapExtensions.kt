package dev.farukh.copyclose.core.utils.extensions

import dev.farukh.copyclose.core.NetworkError
import dev.farukh.copyclose.core.utils.Result
import dev.farukh.network.utils.RequestResult

fun RequestResult.ServerError.asNetworkError(): Result.Error<NetworkError> {
    return Result.Error(NetworkError.ServerError)
}

fun RequestResult.TimeoutError.asNetworkError(): Result.Error<NetworkError> {
    return Result.Error(NetworkError.TimeoutError)
}

fun RequestResult.HostError.asNetworkError(): Result.Error<NetworkError> {
    return Result.Error(NetworkError.HostError)
}

fun RequestResult.Unknown.asNetworkError(): Result.Error<NetworkError> {
    return Result.Error(NetworkError.UnknownError(e))
}

fun RequestResult.ClientError.asNetworkError(): Result.Error<NetworkError> {
    return asUnknownError()
}

fun RequestResult.ClientError.asUnknownError(): Result.Error<NetworkError> {
    return Result.Error(
        NetworkError.UnknownError(Exception("$code\n$errorMessage"))
    )
}

fun <T> RequestResult<T>.asResult() = when (this) {
    is RequestResult.ClientError -> asNetworkError()
    is RequestResult.HostError -> asNetworkError()
    is RequestResult.ServerError -> asNetworkError()
    is RequestResult.TimeoutError -> asNetworkError()
    is RequestResult.Unknown -> asNetworkError()

    is RequestResult.Success -> Result.Success(data)
}