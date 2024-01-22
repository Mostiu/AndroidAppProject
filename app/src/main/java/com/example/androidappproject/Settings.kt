package com.example.androidappproject

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState


enum class TabPage(val icon: ImageVector){
    First(Icons.Default.Info),
    Second(Icons.Default.ThumbUp)
}
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Settings(navController: NavController, mainViewModel: MainViewModel){

    var tabIndex by remember { mutableStateOf(0) }

    val tabs = listOf("Home", "About")
    Column(
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 60.dp)
    ) {
        TabRow(selectedTabIndex = tabIndex) {
            tabs.forEachIndexed { index, title ->
                Tab(text = { Text(title) },
                    selected = tabIndex == index,
                    onClick = { tabIndex = index },
                    icon = {
                        when (index) {
                            0 -> Icon(imageVector = Icons.Default.Home, contentDescription = null)
                            1 -> Icon(imageVector = Icons.Default.Info, contentDescription = null)
                        }
                    }
                )
            }
        }
        when (tabIndex) {
            0 -> FirstPage(mainViewModel)
            1 -> SecondPage(mainViewModel)
        }
    }
}

@Composable
fun FirstPage(mainViewModel: MainViewModel) {
    var username by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
        ) {
            Text(text = "Ustaw Username")
            Spacer(modifier = Modifier.padding(10.dp))
            TextField(value = username, onValueChange = {
                username = it
            })
            Spacer(modifier = Modifier.padding(10.dp))
            Text(text = "Ustaw Description")
            Spacer(modifier = Modifier.padding(10.dp))
            TextField(value = description, onValueChange = {
                description = it
              })
            Spacer(modifier = Modifier.padding(10.dp))
            Button(onClick = {
                mainViewModel.updateUsername(username)
                mainViewModel.updateDescription(description)
            }) {
                Text(text = "Zapisz")
            }
        }
    }
}


@OptIn(ExperimentalPagerApi::class)
@Composable
fun SecondPage(mainViewModel: MainViewModel) {

    val photos = listOf(
        R.drawable.p1,
        R.drawable.p2,
        R.drawable.p3,
        R.drawable.p4,
        R.drawable.p5,
    )
    val pagerState = rememberPagerState()
    Box(modifier = Modifier.fillMaxSize()){
        HorizontalPager(
            count = photos.size,
            state = pagerState,
            key = { photos[it] }
        ) { page ->
            Image(
                painter = painterResource(id = photos[page]),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
                )

        }
        Button(onClick = {
            mainViewModel.updateProfilePicture(pagerState.currentPage.toString())
        }) {
            Text(text = "Zapisz")
        }
    }
}

