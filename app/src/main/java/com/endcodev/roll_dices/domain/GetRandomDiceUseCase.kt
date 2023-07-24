package com.endcodev.roll_dices.domain

import kotlin.random.Random

class GetRandomDiceUseCase {

    /**
     * Generate a random list of dice rolls.
     *
     * @param faces The number of sides on each dice.
     * @param diceQuantity The number of dice rolls to generate.
     * @return A mutable list of random integers representing dice rolls.
     */
    operator fun invoke(faces: Int, diceQuantity: Int): MutableList<Int> {
        val randomNumbers = MutableList(diceQuantity) { Random.nextInt(1, faces + 1) }
        return (randomNumbers)
    }
}