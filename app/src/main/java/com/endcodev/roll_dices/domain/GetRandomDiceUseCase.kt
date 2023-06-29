package com.endcodev.roll_dices.domain

import android.util.Log
import kotlin.random.Random

class GetRandomDiceUseCase {

    companion object {
        const val TAG = "GetRandomDiceUseCase ***"
    }

    operator fun invoke(sides: Int, diceQuantity: Int): MutableList<Int> {
        val randomNumbers = MutableList(diceQuantity) { Random.nextInt(1, sides + 1) }
        Log.v(TAG, "$randomNumbers")
        return (randomNumbers)
    }
}