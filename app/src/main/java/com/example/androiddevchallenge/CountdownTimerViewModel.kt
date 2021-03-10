package com.example.androiddevchallenge

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*

class CountdownTimerViewModel : ViewModel() {

    private val _currentTimeInSecs = MutableLiveData<Int?>(null)
    val currentTimeInSecs: LiveData<Int?> = _currentTimeInSecs

    private val _timerState = MutableLiveData<State>(State.None)
    val timerState: LiveData<State> = _timerState

    private var chosenTime = ChosenTime(0, 0, 0)

    private var timerJob: Job? = null

    fun selectHours(hours: Int) {
       chosenTime = chosenTime.copy(hours = hours)
    }

    fun selectMinutes(minutes: Int) {
        chosenTime = chosenTime.copy(minutes = minutes)
    }

    fun selectSeconds(seconds: Int) {
        chosenTime = chosenTime.copy(seconds = seconds)
    }

    fun startCountdown(hours: Int, minutes: Int, seconds: Int) {
        val totalSeconds = hours * 3600 + minutes * 60 + seconds
        launchCountdownWith(totalSeconds)
    }

    fun pauseCountdown() {
        timerJob?.cancel()
    }

    fun resumeCountdown() {
        val currentSeconds = _currentTimeInSecs.value ?: 0
        launchCountdownWith(currentSeconds)
    }

    fun cancelCountdown() {
        timerJob?.cancel()
        _currentTimeInSecs.postValue(null)
    }

    private fun launchCountdownWith(seconds: Int) {
        timerJob = GlobalScope.launch(Dispatchers.Main) {
            for (passedSeconds in 1..seconds) {
                _currentTimeInSecs.postValue(seconds - passedSeconds)
                delay(1000)
            }
        }
    }

    data class ChosenTime(
        val hours: Int,
        val minutes: Int,
        val seconds: Int
    )

    sealed class State {

        object None : State()

        data class Running(val timeInSecs: Int) : State()

        data class Paused(val timeInSecs: Int) : State()
    }
}