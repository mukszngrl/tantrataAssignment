package com.example.tantrataassignment.model

data class GalleryResponse(
    val `data`: List<Data>,
    val status: Int,
    val success: Boolean
)