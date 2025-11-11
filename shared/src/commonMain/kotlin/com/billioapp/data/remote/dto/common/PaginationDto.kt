package com.billioapp.data.remote.dto.common

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PaginationDto(
    @SerialName("page") val page: Int? = null,
    @SerialName("per_page") val perPage: Int? = null,
    @SerialName("total") val total: Int? = null,
    @SerialName("has_next") val hasNext: Boolean? = null
)