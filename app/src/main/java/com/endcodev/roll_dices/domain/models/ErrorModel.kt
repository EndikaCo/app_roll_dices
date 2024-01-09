package com.endcodev.roll_dices.domain.models

data class ErrorModel(
    val title: String,
    val description: String,
    val acceptButton: String,
    val cancelButton: String,
)
