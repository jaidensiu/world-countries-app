package com.jaidensiu.worldcountriesapp.presentation

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.jaidensiu.worldcountriesapp.R
import org.osmdroid.util.GeoPoint

@Composable
fun MapScreen(
    modifier: Modifier = Modifier,
    country: String,
    latitude: Double,
    longitude: Double,
    navController: NavController
) {
    Scaffold(
       topBar = {
           TopAppBar(
               title = {
                   Text(
                       text = country,
                       fontSize = 32.sp
                   )
               },
               modifier = Modifier.height(96.dp),
               navigationIcon = {
                   IconButton(
                       onClick = {
                           navController.navigateUp()

                       }
                   ) {
                       Icon(
                           painter = painterResource(id = R.drawable.ic_back_arrow),
                           contentDescription = "Back",
                           modifier = Modifier.size(32.dp)
                       )
                   }
               }
           )
       },
        content = {
            val geoPoint by remember {
                mutableStateOf(GeoPoint(latitude, longitude))
            }

            OsmMapView(
                modifier = modifier.padding(it),
                geoPoint = geoPoint
            )
        }
    )
}