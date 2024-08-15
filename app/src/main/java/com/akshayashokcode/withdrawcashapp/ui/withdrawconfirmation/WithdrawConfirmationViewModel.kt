package com.akshayashokcode.withdrawcashapp.ui.withdrawconfirmation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akshayashokcode.withdrawcashapp.data.model.WithdrawRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WithdrawConfirmationViewModel : ViewModel() {
    private val _confirmationCode = MutableLiveData<String>()
    val confirmationCode: LiveData<String> get() = _confirmationCode

    private val _expirationTime = MutableLiveData<Pair<Int, Int>>() // Pair(hours, minutes)
    val expirationTime: LiveData<Pair<Int, Int>> get() = _expirationTime

    init {
        startExpirationTimer()
    }

    fun getConfirmationCode(request: WithdrawRequest?) {
        if (request == null) {
            return
        }
        val apiResponse = fetchConfirmationCodeFromApi(request!!)
        _confirmationCode.value = apiResponse
    }

    private fun fetchConfirmationCodeFromApi(request: WithdrawRequest): String {
        return "3854"
    }

    private fun startExpirationTimer() {

        viewModelScope.launch {
            while (true) {
                // We can set a timer over here and update the UI every minute
                val hours = 2
                val minutes = 5
                _expirationTime.value = Pair(hours, minutes)
                withContext(Dispatchers.IO) {
                    kotlinx.coroutines.delay(60000)
                }
            }
        }
    }
}