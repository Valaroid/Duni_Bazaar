package com.example.dunibazar.model.data

data class LoginResponse (
    val expiresAt: Int,
    val message: String,
    val success: Boolean,
    val token: String?
)
