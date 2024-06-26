package dev.farukh.network.services.copyClose.info.response

import dev.farukh.network.core.AddressCore
import dev.farukh.network.core.RoleCore
import dev.farukh.network.core.ServiceCore
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class UserInfoResponse(
    @SerialName("user_id")
    val userID: String,
    @SerialName("auth_token")
    val authToken: String,
    @SerialName("name")
    val name: String,
    @SerialName("user_image")
    val imageID: String,
    @SerialName("role")
    val role: RoleCore,
    @SerialName("address")
    val address: AddressCore,
    @SerialName("services")
    val services: List<ServiceCore> = emptyList()
)