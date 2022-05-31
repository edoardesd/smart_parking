package com.example.authentication

data class GResponseModel(
    val distance: Field ? = null,
    val duration: Field ? = null,
    val status: String ? = null
)

data class Field(
    val text: String ? = null,
    val value: String ? = null
)