package com.deprojectmain.tunemood

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.deprojectmain.tunemood.navigation.AccountScreenClass
import com.deprojectmain.tunemood.navigation.AlbumScreenClass
import com.deprojectmain.tunemood.navigation.ArtistScreenClass
import com.deprojectmain.tunemood.navigation.BrowseScreenClass
import com.deprojectmain.tunemood.navigation.LibraryScreenClass
import com.deprojectmain.tunemood.navigation.MainScreenClass
import com.deprojectmain.tunemood.navigation.SettingsScreenClass
import com.deprojectmain.tunemood.navigation.TrackPlayerScreenClass
import com.deprojectmain.tunemood.screens.AccountScreen
import com.deprojectmain.tunemood.screens.AlbumScreen
import com.deprojectmain.tunemood.screens.ArtistScreen
import com.deprojectmain.tunemood.screens.BrowseScreen
import com.deprojectmain.tunemood.screens.LibraryScreen
import com.deprojectmain.tunemood.screens.MainScreen
import com.deprojectmain.tunemood.screens.SettingsScreen
import com.deprojectmain.tunemood.screens.TrackPlayerScreen
import com.deprojectmain.tunemood.ui.theme.TuneMoodTheme
import com.deprojectmain.tunemood.viewmodel.APIRepository
import com.deprojectmain.tunemood.viewmodel.APInterface
import com.deprojectmain.tunemood.viewmodel.MainViewModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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

                    // Check internet permission
                    val isConnectedToInternet = remember {
                        mutableStateOf(
                            ContextCompat.checkSelfPermission(
                                context,
                                android.Manifest.permission.INTERNET
                            ) == PackageManager.PERMISSION_GRANTED
                        )
                    }

                    if (!isConnectedToInternet.value) {
                        Toast.makeText(
                            context,
                            "You do not have internet access! App cannot work without internet.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    // Step 1: Create Retrofit instance
                    val retrofit = remember {
                        Retrofit.Builder()
                            .baseUrl("https://deezerdevs-deezer.p.rapidapi.com/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build()
                            .create(APInterface::class.java)
                    }

                    // ViewModel initialization
                    val model = remember {
                        MainViewModel(repository = APIRepository(retrofit))
                    }

                    // Fetch data using coroutine
                    LaunchedEffect(key1 = model) {
                        model.fetchData()
                    }
                    val dataList = model.data.collectAsState()
                    val navController = rememberNavController()
                    val startScreen = remember {
                        mutableStateOf("TuneMood")
                    }
                    if (model.isLoading.collectAsState().value) {
                        Column(
                            modifier = Modifier.wrapContentSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = Color(0xFF68B7E8) // Adjust color as needed
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Loading...", // Optional loading message
                                style = MaterialTheme.typography.titleSmall,
                                color = Color.Gray,
                                textAlign = TextAlign.Center
                            )
                        }
                    } else {
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
                                    MainScreen(navController, dataList.value)
                                }
                                composable<BrowseScreenClass> {
                                    startScreen.value = "Browse"
                                    BrowseScreen(navController)
                                }
                                composable<LibraryScreenClass> {
                                    startScreen.value = "Library"
                                    LibraryScreen(navController, model)
                                }
                                composable<SettingsScreenClass> {
                                    startScreen.value = "Settings"
                                    SettingsScreen(navController)
                                }
                                composable<AlbumScreenClass> { item ->
                                    val args = item.toRoute<AlbumScreenClass>()
                                    val albumsId = args.id
                                    val albumsTitle: String = args.title
//                                    val albumTrackList:
                                    val albumArtist: String = args.artist
                                    val albumCover: String = args.cover
                                    AlbumScreen(
                                        navController = navController,
                                        id = albumsId,
                                        title = albumsTitle,
                                        artist = albumArtist,
                                        cover = albumCover,
                                        viewModel = model,
                                    )
                                }
                                composable<ArtistScreenClass> { items ->
                                    val args = items.toRoute<ArtistScreenClass>()
                                    val artistId = args.id
                                    val artistName = args.name
                                    val artistPicture = args.picture
                                    ArtistScreen(
                                        navController = navController,
                                        id = artistId,
                                        title = artistName,
                                        cover = artistPicture,
                                        viewModel = model,
                                    )

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
}