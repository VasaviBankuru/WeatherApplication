package com.example.weather.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.weather.viewmodel.WeatherViewModel
import com.example.weather.utils.NetworkResponse
import com.example.weather.model.WeatherModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun WeatherPage(weatherViewModel: WeatherViewModel) {
    var city by remember { mutableStateOf("") }
    val weatherResult = weatherViewModel.weatherResult.observeAsState()
    val keyboardController = LocalSoftwareKeyboardController.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Location Weather", fontSize = 30.sp, fontWeight = FontWeight.Bold)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(modifier = Modifier.weight(1f),
                value = city,
                onValueChange = { city = it },
                label = { Text(text = "Enter Location") })
            IconButton(onClick = {
                keyboardController?.hide()
                weatherViewModel.getCityWeather(city)
            }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search For Any Location"
                )

            }
        }

        when (val result = weatherResult.value) {
            NetworkResponse.Loading -> {
                CircularProgressIndicator()
            }
            is NetworkResponse.Error -> {
                Text(text = result.message)
            }
            is NetworkResponse.Success -> {
                WeatherDetails(data = result.data)
            }

            else -> {}
        }
    }
}

@Composable
fun WeatherDetails(data: WeatherModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Bottom
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "Location Icon",
                modifier = Modifier.size(40.dp)
            )
            Text(text = data.location.name, fontSize = 30.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = data.location.country, fontSize = 16.sp, color = Color.LightGray)
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = "${data.current.temp_c} ° C",
            fontSize = 56.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        AsyncImage(
            model = "https:${data.current.condition.icon}".replace("64*64", "128*128"),
            contentDescription = "Condition Icon",
            modifier = Modifier.size(160.dp),
        )

        Text(
            text = data.current.condition.text,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.width(30.dp))
        Card {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.LightGray.copy(alpha = 0.2f))
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    WeatherKeyValue("Humidity", data.current.humidity)
                    WeatherKeyValue("Wind Speed", data.current.wind_kph + "km/h")
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    WeatherKeyValue("UV", data.current.uv)
                    WeatherKeyValue("Participation", data.current.precip_mm + "mm")
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    WeatherKeyValue("Local Time", data.location.localtime.split(" ")[1])
                    WeatherKeyValue("Local Date", data.location.localtime.split(" ")[0])
                }
            }

        }
    }
}

@Composable
fun WeatherKeyValue(key: String, value: String) {
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = value, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text(text = key, fontWeight = FontWeight.SemiBold, color = Color.Gray)
    }
}