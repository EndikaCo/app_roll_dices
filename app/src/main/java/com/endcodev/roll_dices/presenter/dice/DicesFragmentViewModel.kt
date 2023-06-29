package com.endcodev.roll_dices.presenter.dice

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.endcodev.roll_dices.domain.GetRandomDiceUseCase

class DicesFragmentViewModel : ViewModel() {

    companion object {
        const val TAG = "DicesFragmentViewModel ***"
    }

    private val _diceList: MutableLiveData<MutableList<Int>> by lazy {
        MutableLiveData<MutableList<Int>>().apply {
            value = mutableListOf(1)
        }
    }
    val diceList: LiveData<MutableList<Int>> get() = _diceList

    fun rollDices(sides: Int, diceQuantity: Int) {
        _diceList.value = GetRandomDiceUseCase().invoke(sides, diceQuantity)
    }

    fun removeDice() {
        _diceList.value?.let {
            if (it.isNotEmpty()) {
                it.removeAt(it.lastIndex)
            }
        }
    }

    fun addDice(num: Int) {
        _diceList.value?.add(num)
    }
}