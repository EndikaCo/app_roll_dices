package com.endcodev.roll_dices.data.repository

import kotlin.random.Random

class RandomRepository {

    private val random = Random.Default

    /**
     * Generate a random list of dice rolls.
     *
     * @param faces The number of sides on each dice.
     * @param diceQuantity The number of dice rolls to generate.
     * @return A list of random integers representing dice rolls.
     */
    operator fun invoke(faces: Int, diceQuantity: Int): List<Int> {
        val result = ArrayList<Int>(diceQuantity)
        repeat(diceQuantity) {
            result.add(random.nextInt(1, faces + 1))
        }
        return result
    }
}