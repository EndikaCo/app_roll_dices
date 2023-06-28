package com.endcodev.roll_dices.presenter.dice

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.endcodev.roll_dices.domain.GetRandomDiceUseCase

class DicesFragmentViewModel : ViewModel() {

    private val _diceFace = MutableLiveData<List<Int>>()
    val diceFace: LiveData<List<Int>> get() = _diceFace

    private var isRolling = false
    private var maxDices = 5


    fun setMaxDices(max : Int){
        maxDices = max
    }

    fun isRolling(state : Boolean){
        isRolling = state
    }

    fun rollDices(sides: Int, diceQuantity: Int){
        _diceFace.value = GetRandomDiceUseCase().invoke(sides, diceQuantity)
    }

}