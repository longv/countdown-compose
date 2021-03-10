/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.example.androiddevchallenge.ui.theme.MyTheme
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private val viewModel by lazy {
        ViewModelProvider(this)
            .get(CountdownTimerViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTheme {
                MyApp(viewModel)
            }
        }
    }
}

// Start building your app here!
@Composable
fun MyApp(viewModel: CountdownTimerViewModel) {
    Surface(color = MaterialTheme.colors.background) {
        CountdownTimer(
            onHoursSelected = { viewModel.selectHours(it) },
            onMinutesSelected = { viewModel.selectMinutes(it) },
            onSecondsSelected = { viewModel.selectSeconds(it) },
            "",
            {},
            "",
            {},
            null
        )
    }
}

@Preview("Light Theme", widthDp = 360, heightDp = 640)
@Composable
fun LightPreview() {
    MyTheme {
        MyApp(CountdownTimerViewModel())
    }
}

@Preview("Dark Theme", widthDp = 360, heightDp = 640)
@Composable
fun DarkPreview() {
    MyTheme(darkTheme = true) {
        MyApp(CountdownTimerViewModel())
    }
}

@Composable
fun CountdownTimer(
    onHoursSelected: (Int) -> Unit,
    onMinutesSelected: (Int) -> Unit,
    onSecondsSelected: (Int) -> Unit,
    positiveButtonText: String,
    onPositiveClick: () -> Unit,
    negativeButtonText: String,
    onNegativeClick: (() -> Unit),
    currentTimeInSecs: Int?
) {
    Box(
        modifier = Modifier.background(Color.Yellow)
    ) {
        val hourRange = 0.rangeTo(24)
        val hourState = rememberLazyListState()
        LaunchedEffect(hourState) {
            val selectedHours = hourRange.toList()[hourState.firstVisibleItemIndex]
            onHoursSelected(selectedHours)
        }

        val minuteRange = 0.rangeTo(59)
        val minuteState = rememberLazyListState()
        LaunchedEffect(minuteState) {
            val selectedMinutes = minuteRange.toList()[minuteState.firstVisibleItemIndex]
            onMinutesSelected(selectedMinutes)
        }

        val secondRange = 0.rangeTo(59)
        val secondState = rememberLazyListState()
        LaunchedEffect(secondState) {
            val selectedSeconds = secondRange.toList()[secondState.firstVisibleItemIndex]
            onSecondsSelected(selectedSeconds)
        }

        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.5f)
                    .padding(top = 16.dp)
                    .background(Color.Cyan),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                TimeSelection(state = hourState, range = 0.rangeTo(24), unit = TimeUnit.HOURS)
                TimeSelection(state = minuteState, range = 0.rangeTo(59), unit = TimeUnit.MINUTES)
                TimeSelection(state = secondState, range = 0.rangeTo(59), unit = TimeUnit.SECONDS)
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Button(
                    onClick = onNegativeClick,
                    enabled = currentTimeInSecs != null
                ) {
                    Text(text = negativeButtonText)
                }
                Button(
                    onClick = onPositiveClick
                ) {
                    Text(text = positiveButtonText)
                }
            }
        }
    }
}

@Composable
fun TimeSelection(state: LazyListState, range: IntRange, unit: TimeUnit) {
    LazyColumn(state = state) {
        items(range.toList()) { value ->
            Text("$value")
        }
    }
}