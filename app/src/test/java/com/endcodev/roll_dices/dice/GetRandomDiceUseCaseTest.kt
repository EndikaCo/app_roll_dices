package com.endcodev.roll_dices.dice

import com.endcodev.roll_dices.domain.usecases.GetRandomDiceUseCase
import junit.framework.TestCase
import org.junit.Test

internal class GetRandomDiceUseCaseTest {

    @Test
    fun `check if dices roll result is enough random`() {
        // Define test parameters
        val testQuantity = 1000 // The number of tests to run
        val min = (testQuantity * 0.7) // Minimum expected frequency (70% of testQuantity)
        val max = (testQuantity * 0.9) // Maximum expected frequency (90% of testQuantity)
        val faces = 6 // The number of sides on each dice (for 6 faces use 7)
        val randNumList = ArrayList<Int>()

        // Perform the tests
        for (i in 1..testQuantity) {
            // Generate 5 dice rolls with 6 faces each
            val a = GetRandomDiceUseCase().invoke(faces, 5)
            for (item in a)
                randNumList.add(item)
        }
        // Create a list to keep track of the frequency of each roll result
        val rank = MutableList(faces) { 0 }

        // Count the frequency of each roll result
        for (i in randNumList.indices)
            rank[randNumList[i] - 1] = rank[randNumList[i] - 1] + 1

        // Check if the frequency of each roll result is within the expected range
        for (i in rank.indices)
            TestCase.assertTrue(rank[i] in min.toInt()..max.toInt())
    }
}

