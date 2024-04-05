package dev.farukh.copyclose.core.data.repos

import dev.farukh.copyclose.core.AuthError
import dev.farukh.copyclose.core.NetworkError
import dev.farukh.copyclose.core.data.model.Address
import dev.farukh.copyclose.utils.Result
import dev.farukh.copyclose.utils.extensions.asNetworkError
import dev.farukh.network.core.AddressCore
import dev.farukh.network.services.copyClose.authService.AuthService
import dev.farukh.network.services.copyClose.authService.request.RegisterRequest
import dev.farukh.network.services.copyClose.authService.response.RegisterResponse
import dev.farukh.network.utils.RequestResult

class AuthRepository(private val authService: AuthService) {
    suspend fun register(
        login: String,
        name: String,
        password: String,
        address: Address,
        image: ByteArray,
        isSeller: Boolean,
    ): Result<RegisterResponse, Unit> {
        val registerResult = authService.register(
            RegisterRequest(
                login = login,
                password = password,
                address = AddressCore(
                    lat = address.lat,
                    lon = address.lon,
                    addressName = address.addressName
                ),
                name = name,
                isSeller = isSeller
            ),
            image = image,
        )

        return when (registerResult) {
            is RequestResult.Success -> Result.Success(registerResult.data)
            else -> Result.Error(Unit)
        }
    }

    suspend fun logIn(login: String, password: String): Result<String, NetworkError> {
        return when (val loginResult = authService.logIn(login, password)) {
            is RequestResult.ClientError -> loginResult.asNetworkError()
            is RequestResult.HostError -> loginResult.asNetworkError()
            is RequestResult.ServerError -> loginResult.asNetworkError()
            is RequestResult.TimeoutError -> loginResult.asNetworkError()
            is RequestResult.Unknown -> loginResult.asNetworkError()

            is RequestResult.Success -> Result.Success(loginResult.data)
        }
    }
}

private fun RequestResult.ClientError.asNetworkError() = Result.Error(
    when (code) {
        401 -> AuthError.LoginError
        else -> NetworkError.UnknownError(Exception(errorMessage))
    }
)
