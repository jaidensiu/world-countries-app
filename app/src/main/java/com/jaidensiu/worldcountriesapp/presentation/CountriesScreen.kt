package com.jaidensiu.worldcountriesapp.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.jaidensiu.worldcountriesapp.R
import com.jaidensiu.worldcountriesapp.domain.DetailedCountry
import com.jaidensiu.worldcountriesapp.domain.SimpleCountry

@Composable
fun CountriesScreen(
    modifier: Modifier = Modifier,
    countriesState: CountriesState,
    countrySearchBarState: CountrySearchBarState,
    onSelectCountry: (code: String) -> Unit,
    onDismissCountryDialog: () -> Unit,
    onFilterCountries: (String) -> Unit,
    onResetCountries: () -> Unit,
    onToggleSearchBar: () -> Unit,
    onUpdateSearchQuery: (String) -> Unit,
    navController: NavController
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (countrySearchBarState.showSearchBar) {
                        TextField(
                            value = countrySearchBarState.searchQuery,
                            onValueChange = {
                                if (it.length <= 15) {
                                    onUpdateSearchQuery(it)
                                    onFilterCountries(it)
                                }
                            },
                            maxLines = 1,
                            textStyle = TextStyle(fontSize = 24.sp, color = Color.White),
                            colors = TextFieldDefaults.textFieldColors(
                                cursorColor = Color.White
                            )
                        )
                    } else {
                        Text(
                            text = "World Countries",
                            fontSize = 32.sp
                        )
                    }
                },
                modifier = Modifier.height(96.dp),
                navigationIcon = {
                    IconButton(
                        onClick = {
                            if (countrySearchBarState.showSearchBar) {
                                onUpdateSearchQuery("")
                                onResetCountries()
                            }
                            onToggleSearchBar()
                        },
                        modifier = Modifier.padding(horizontal = 12.dp)
                    ) {
                        if (countrySearchBarState.showSearchBar) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_back_arrow),
                                contentDescription = "Back arrow icon",
                                modifier = Modifier.size(32.dp)
                            )
                        } else {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_search),
                                contentDescription = "Search icon",
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                },
                backgroundColor = MaterialTheme.colors.primary
            )
        },
        content = { paddingValues ->
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                if (countriesState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .testTag(tag = "progressIndicator")
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(countriesState.countries) { country ->
                            CountryItem(
                                country = country,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onSelectCountry(country.code)
                                    }
                                    .padding(16.dp)
                            )
                        }
                    }

                    if (countriesState.selectedCountry != null) {
                        CountryDialog(
                            modifier = Modifier
                                .clip(RoundedCornerShape(5.dp))
                                .background(Color.White)
                                .padding(16.dp),
                            country = countriesState.selectedCountry,
                            onDismiss = {
                                onDismissCountryDialog()
                            },
                            navController = navController
                        )
                    }
                }
            }
        }
    )
}

@Composable
private fun CountryDialog(
    modifier: Modifier = Modifier,
    country: DetailedCountry,
    onDismiss: () -> Unit,
    navController: NavController
) {
    val joinedLanguages = remember(country.languages) {
        country.languages.joinToString()
    }
    val countryDetailsMap = mapOf(
        "Continent" to country.continent,
        "Currency" to country.currency,
        "Capital" to country.capital,
        "Language(s)" to joinedLanguages
    )

    Dialog(
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = modifier
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Text(
                    text = country.emoji,
                    fontSize = 24.sp
                )
                Spacer(
                    modifier = Modifier.width(16.dp)
                )
                Text(
                    text = country.name,
                    fontSize = 24.sp
                )
            }
            for ((label, value) in countryDetailsMap) {
                Text(text = "$label: $value")
                Spacer(modifier = Modifier.height(8.dp))
            }
            Button(
                onClick = {
                    navController.navigate(route = "mapScreen")
                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 16.dp),
                elevation = ButtonDefaults.elevation(
                    defaultElevation = 0.dp,
                    pressedElevation = 0.dp,
                    disabledElevation = 0.dp,
                    hoveredElevation = 0.dp,
                    focusedElevation = 0.dp
                ),
                content = {
                    Text(
                        text = "View on Map",
                        fontSize = 16.sp
                    )
                }
            )
        }
    }
}

@Composable
private fun CountryItem(
    modifier: Modifier = Modifier,
    country: SimpleCountry
) {
    Row(
        modifier = modifier.testTag("countryItem"),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = country.emoji,
            fontSize = 30.sp
        )
        Spacer(
            modifier = Modifier.width(16.dp)
        )
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = country.name,
                fontSize = 24.sp
            )
            Spacer(
                modifier = Modifier.width(16.dp)
            )
            Text(
                text = country.capital,
                fontSize = 20.sp
            )
        }
    }
}