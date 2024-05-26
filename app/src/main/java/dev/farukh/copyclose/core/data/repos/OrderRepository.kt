package dev.farukh.copyclose.core.data.repos

import dev.farukh.copyclose.core.NetworkError
import dev.farukh.copyclose.core.utils.Result
import dev.farukh.copyclose.core.utils.extensions.asResult
import dev.farukh.copyclose.features.order.creation.data.dto.OrderCreationDTO
import dev.farukh.network.services.copyClose.info.InfoService
import dev.farukh.network.services.copyClose.order.OrderService
import dev.farukh.network.services.copyClose.order.request.OrderCreationRequest

class OrderRepository(
    private val orderService: OrderService,
    private val infoService: InfoService
) {
    suspend fun createOrder(
        dto: OrderCreationDTO,
        attachmentIds: List<String>,
    ): Result<Unit, NetworkError> {
        val createResult = orderService.createOrder(
            OrderCreationRequest(
                userID = dto.userID,
                sellerID = dto.sellerID,
                attachments = attachmentIds,
                services = dto.services,
                comment = dto.comment,
                authToken = dto.authToken
            )
        )
        return createResult.asResult()
    }

    suspend fun getOrdersFor(userID: String, authToken: String) =
        infoService.getOrderInfoFor(userID, authToken).asResult()
}