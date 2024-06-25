package com.deprojectmain.tunemood.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun LibraryScreen(
    navController: NavController
) {

}

@Preview(showBackground = true)
@Composable
fun LibraryScreenPreview() {
    val navController = rememberNavController()
    LibraryScreen(navController = navController)
}