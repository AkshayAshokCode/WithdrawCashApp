package com.akshayashokcode.withdrawcashapp.ui.withdrawrequest

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class WithdrawRequestViewModel : ViewModel() {

    private val _amount = MutableLiveData<Double?>()
    val amount: MutableLiveData<Double?> get() = _amount

    private val _availableBalance = MutableLiveData<Double>()
    val availableBalance: LiveData<Double> get() = _availableBalance

    private val _processingFee = MutableLiveData<Double>()
    val processingFee: LiveData<Double> get() = _processingFee

    private val _totalAmount = MutableLiveData<Double>()
    val totalAmount: LiveData<Double> get() = _totalAmount

    init {
        // dummy values
        _availableBalance.value = 130.0
        _processingFee.value = 0.750
    }

    fun onAmountChanged(newAmount: Double?) {
        _amount.value = newAmount
        calculateTotalAmount()
    }

    private fun calculateTotalAmount() {
        val amount = _amount.value ?: 0.0
        val processingFee = _processingFee.value ?: 0.0
        val total = amount + processingFee

        _totalAmount.value = total
    }
}