package com.example.artspace

import androidx.annotation.DrawableRes

data class Artwork(
    val id: Int,
    @DrawableRes val imageRes: Int,
    val title: String,
    val artist: String,
    val year: String
)