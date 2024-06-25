package com.deprojectmain.tunemood.data

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse<T>(
    val data: List<T>
)