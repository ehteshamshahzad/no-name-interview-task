package com.ehtesham.entity

import kotlinx.serialization.Serializable

@Serializable
data class Streak(
    val free_days: Int
)
