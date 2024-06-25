package com.deprojectmain.tunemood

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.deprojectmain.tunemood.data.Data
import com.deprojectmain.tunemood.navigation.AccountScreenClass
import com.deprojectmain.tunemood.navigation.AlbumScreenClass
import com.deprojectmain.tunemood.navigation.BrowseScreenClass
import com.deprojectmain.tunemood.navigation.LibraryScreenClass
import com.deprojectmain.tunemood.navigation.MainScreenClass
import com.deprojectmain.tunemood.navigation.SettingsScreenClass
import com.deprojectmain.tunemood.navigation.TrackPlayerScreenClass
import com.deprojectmain.tunemood.screens.AccountScreen
import com.deprojectmain.tunemood.screens.AlbumScreen
import com.deprojectmain.tunemood.screens.BrowseScreen
import com.deprojectmain.tunemood.screens.LibraryScreen
import com.deprojectmain.tunemood.screens.MainScreen
import com.deprojectmain.tunemood.screens.SettingsScreen
import com.deprojectmain.tunemood.screens.TrackPlayerScreen
import com.deprojectmain.tunemood.ui.theme.TuneMoodTheme
import com.deprojectmain.tunemood.viewmodel.MainViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @SuppressLint("CoroutineCreationDuringComposition")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TuneMoodTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.surface
                ) { // A surface container using the 'background' color from the theme
                    val context = LocalContext.current
                    val isConnectedToInternet = remember {
                        mutableStateOf(
                            ContextCompat.checkSelfPermission(
                                context,
                                android.Manifest.permission.INTERNET
                            ) == PackageManager.PERMISSION_GRANTED
                        )
                    }
                    if (isConnectedToInternet.value) {
                        Log.d("Start", "onCreate: You have internet access")
                    } else {
                        Toast.makeText(
                            context,
                            "onCreate: You do not have internet access! \n App Can't Work Without Internet!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    val viewModel: MainViewModel by viewModels()
                    var dataList = mutableListOf<Data>()
                    lifecycleScope.launch {
                        try {
                            dataList = viewModel.collectDataFromAPI().toMutableList()
                            Log.d("Collection Of Data", "Data collected from API!")
                        } catch (e: Exception) {
                            Log.d("Collection Of Data", "Failed to collect data: $e")
                        }
                    }
                    val navController = rememberNavController()
                    val startScreen = remember {
                        mutableStateOf("TuneMood")
                    }
                    Scaffold(modifier = Modifier
                        .fillMaxSize(),
                        topBar = {
                            TopAppBar(
                                title = {
                                    Text(text = startScreen.value)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(2.dp)
                                    .wrapContentHeight(),
                                navigationIcon = {
                                    IconButton(onClick = { navController.navigateUp() }) {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                            contentDescription = "Back"
                                        )
                                    }
                                }, colors = TopAppBarColors(
                                    containerColor = Color(0xFF68B7E8),
                                    actionIconContentColor = Color.Black,
                                    scrolledContainerColor = Color.Black,
                                    navigationIconContentColor = Color.White,
                                    titleContentColor = Color.White
                                )
                            )
                        },
                        bottomBar = {
                            BottomAppBar(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight(),
                                containerColor = Color(0xFF68B7E8),
                                tonalElevation = 4.dp
                            ) {
                                IconButton(
                                    onClick = { navController.navigate(MainScreenClass) },
                                    modifier = Modifier
                                        .weight(1f)
                                        .wrapContentHeight()
                                        .padding(2.dp)
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier
                                            .wrapContentSize()
                                            .padding(2.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Home,
                                            contentDescription = "Home",
                                            tint = Color.White
                                        )
                                        Text(
                                            text = "Home",
                                            color = Color.White,
                                            style = MaterialTheme.typography.labelSmall
                                        )
                                    }
                                }
                                IconButton(
                                    onClick = { navController.navigate(LibraryScreenClass) },
                                    modifier = Modifier
                                        .weight(1f)
                                        .wrapContentHeight()
                                        .padding(2.dp)
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier
                                            .wrapContentSize()
                                            .padding(2.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Add,
                                            contentDescription = "Library",
                                            tint = Color.White
                                        )
                                        Text(
                                            text = "Library",
                                            color = Color.White,
                                            style = MaterialTheme.typography.labelSmall
                                        )
                                    }
                                }
                                IconButton(
                                    onClick = { navController.navigate(BrowseScreenClass) },
                                    modifier = Modifier
                                        .weight(1f)
                                        .wrapContentHeight()
                                        .padding(2.dp)
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier
                                            .wrapContentSize()
                                            .padding(2.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Search,
                                            contentDescription = "Browse",
                                            tint = Color.White
                                        )
                                        Text(
                                            text = "Browse",
                                            color = Color.White,
                                            style = MaterialTheme.typography.labelSmall
                                        )
                                    }
                                }
                                IconButton(
                                    onClick = { navController.navigate(AccountScreenClass) },
                                    modifier = Modifier
                                        .weight(1f)
                                        .wrapContentHeight()
                                        .padding(2.dp)
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier
                                            .wrapContentSize()
                                            .padding(2.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.AccountCircle,
                                            contentDescription = "Account",
                                            tint = Color.White
                                        )
                                        Text(
                                            text = "Account",
                                            color = Color.White,
                                            style = MaterialTheme.typography.labelSmall
                                        )
                                    }
                                }
                                IconButton(
                                    onClick = { navController.navigate(SettingsScreenClass) },
                                    modifier = Modifier
                                        .weight(1f)
                                        .wrapContentHeight()
                                        .padding(2.dp)
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier
                                            .wrapContentSize()
                                            .padding(2.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Settings,
                                            contentDescription = "Settings",
                                            tint = Color.White
                                        )
                                        Text(
                                            text = "Settings",
                                            color = Color.White,
                                            style = MaterialTheme.typography.labelSmall
                                        )
                                    }
                                }
                            }
                        }) {
                        NavHost(
                            navController = navController,
                            startDestination = MainScreenClass,
                            modifier = Modifier.padding((it))
                        ) {
                            composable<AccountScreenClass> {
                                startScreen.value = "Account"
                                AccountScreen(navController)
                            }
                            composable<MainScreenClass> {
                                startScreen.value = "TuneMood"
                                MainScreen(navController, dataList.toList())
                            }
                            composable<BrowseScreenClass> {
                                startScreen.value = "Browse"
                                BrowseScreen(navController)
                            }
                            composable<LibraryScreenClass> {
                                startScreen.value = "Library"
                                LibraryScreen(navController)
                            }
                            composable<SettingsScreenClass> {
                                startScreen.value = "Settings"
                                SettingsScreen(navController)
                            }
                            composable<AlbumScreenClass> { item ->
                                val args = item.toRoute<AlbumScreenClass>()
                                val cover: String = args.cover
                                val title: String = args.title
                                val trackList: String = args.trackList
                                val id: Long = args.id
                                val artist: String = args.artist
                                AlbumScreen(navController, cover, title, trackList, id, artist)
                            }
                            composable<TrackPlayerScreenClass> { item ->
                                val args = item.toRoute<TrackPlayerScreenClass>()
                                val id = args.id
                                val trackArtistName = args.trackArtistName
                                val trackAlbumCover = args.trackAlbumCover
                                val trackLink = args.trackLink
                                val trackTitle = args.trackTitle
                                startScreen.value = "TuneMood"
                                TrackPlayerScreen(id,
                                    trackArtistName,
                                    trackAlbumCover,
                                    trackLink,
                                    trackTitle,
                                    {},
                                    {})
                            }
                        }
                    }
                }
            }
        }
    }
}
