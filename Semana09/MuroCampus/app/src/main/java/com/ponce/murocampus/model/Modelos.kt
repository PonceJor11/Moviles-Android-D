package com.ponce.murocampus.model

import kotlinx.serialization.Serializable

@Serializable
data class Post(
    val userId: Int,
    val id: Int,
    val title: String,
    val body: String
)

@Serializable
data class Comentario(
    val postId: Int,
    val id: Int,
    val name: String,
    val email: String,
    val body: String
)

/** Lo que enviamos al crear: no lleva id (lo asigna el servidor). */
@Serializable
data class NuevoPost(
    val userId: Int,
    val title: String,
    val body: String
)

@Serializable
data class Usuario(
    val id: Int,
    val name: String,
    val email: String
)
