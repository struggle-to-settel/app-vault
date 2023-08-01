package com.example.test

data class Videos(
    val categories: List<Category>
)

data class Category(
    val name: String,
    val videos: List<Video>
)

data class Video(
    val description: String,
    val sources: List<String>,
    val subtitle: String,
    val thumb: String,
    val title: String
)


data class UserResponse(
    val `data`: List<User>
)

data class User(
    val __v: Int,
    val _id: String,
    val createdAt: String,
    val email: String,
    val profile: String?,
    val updatedAt: String,
    val userId: String,
    val userName: String
)