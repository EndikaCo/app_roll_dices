package com.endcodev.roll_dices.presentation.dice

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.endcodev.name_draw.domain.utils.App
import com.endcodev.roll_dices.domain.usecases.GetRandomDiceUseCase

class DicesFragmentViewModel : ViewModel() {

    private val _diceList: MutableLiveData<MutableList<Int>> by lazy {
        MutableLiveData<MutableList<Int>>().apply {
            value = mutableListOf(1)
        }
    }
    val diceList: LiveData<MutableList<Int>> get() = _diceList

    private var _sumList = MutableLiveData<MutableList<Int>>()
    val sumList: LiveData<MutableList<Int>> get() = _sumList

    /**
     * Initializes the ViewModel with an empty list for storing dice sums.
     */
    init {
        _sumList.value = mutableListOf()
    }

    /**
     * Rolls dices and updates the dice list with the result.
     * Also adds the sum of the rolled dices to the sum list.
     *
     * @param sides The number of sides for each dice.
     * @param diceQuantity The number of dices to roll.
     */
    fun rollDices(sides: Int, diceQuantity: Int) {
        _diceList.value = GetRandomDiceUseCase().invoke(sides, diceQuantity).toMutableList()
        addToSumList()
    }

    /**
     * Adds the sum of the rolled dices to the sum list.
     */
    private fun addToSumList() {

        val diceList = _diceList.value ?: return
        val sumList = _sumList.value ?: return

        var sum = 0
        for (item in diceList)
            sum += item
        sumList.add(sum)

        Log.v(App.tag, "sum list: ${_sumList.value}")
    }

    /**
     * Removes the last dice from the dice list if the list is not empty.
     */
    fun removeDice() {
        _diceList.value?.let {
            if (it.isNotEmpty()) {
                it.removeAt(it.lastIndex)
            }
        }
    }

    /**
     * Adds a new dice with the given number to the dice list.
     * @param num The number to add as a new dice.
     */
    fun addDice(num: Int) {
        _diceList.value?.add(num)
    }
}