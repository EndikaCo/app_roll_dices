package com.endcodev.roll_dices.domain

import kotlin.random.Random

class GetRandomDiceUseCase {

    operator fun invoke(sides: Int, diceQuantity: Int): List<Int> {
        val randomNumbers = List(diceQuantity) { Random.nextInt(1, sides) }
        return (randomNumbers)
    }
}