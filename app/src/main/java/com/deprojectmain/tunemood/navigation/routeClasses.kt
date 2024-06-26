package com.deprojectmain.tunemood.navigation

import kotlinx.serialization.Serializable

val ScreenList = listOf(
    "MainScreenClass",
    "AccountScreenClass",
    "BrowseScreenClass",
    "LibraryScreenClass",
    "SettingsScreenClass",
    "TrackPlayerScreenClass",
)

@Serializable
object MainScreenClass

@Serializable
object AccountScreenClass

@Serializable
object BrowseScreenClass

@Serializable
object LibraryScreenClass

@Serializable
object SettingsScreenClass

@Serializable
data class TrackPlayerScreenClass(
    val trackTitle: String,
    val trackArtistName: String,
    val trackAlbumCover: String,
    val trackLink: String,
    val id: Long,
)

@Serializable
data class AlbumScreenClass(
    val cover : String,
    val title : String,
    val trackList : String,
    val id : Long,
    val artist : String,
)