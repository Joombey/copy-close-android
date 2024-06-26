package dev.farukh.copyclose.features.order.list.data.dto

import dev.farukh.copyclose.core.data.models.Service

class OrderDTO(
    val orderID: String,
    val name: String,
    val id: String,
    val addressName: String,
    val icon: ByteArray,
    val services: List<Service>,
    val comment: String,
    val state: OrderState,
    val attachments: List<Attachment>,
    val reported: Boolean,
)