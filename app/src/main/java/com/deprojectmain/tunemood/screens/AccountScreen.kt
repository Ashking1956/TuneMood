package com.deprojectmain.tunemood.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
//
@Composable
fun AccountScreen(
    navController: NavController
) {

}

@Preview(showBackground = true)
@Composable
fun AccountScreenPreview() {
    val navController = rememberNavController()
    AccountScreen(navController = navController)
}