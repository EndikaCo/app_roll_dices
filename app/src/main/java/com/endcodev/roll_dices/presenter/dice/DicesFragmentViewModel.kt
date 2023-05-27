package com.endcodev.roll_dices.presenter.dice

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.endcodev.roll_dices.domain.GetRandomDiceUseCase

class DicesFragmentViewModel : ViewModel() {

    private val _diceFace = MutableLiveData<List<Int>>()
    val diceFace: LiveData<List<Int>> get() = _diceFace

    fun rollDices(sides: Int, diceQuantity: Int): List<Int> {
        _diceFace.value = GetRandomDiceUseCase().invoke(sides, diceQuantity)
        return _diceFace.value!!
    }
}