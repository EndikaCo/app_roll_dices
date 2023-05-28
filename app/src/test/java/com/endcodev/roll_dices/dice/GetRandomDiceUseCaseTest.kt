package com.endcodev.roll_dices.dice

import com.endcodev.roll_dices.domain.GetRandomDiceUseCase
import junit.framework.TestCase
import org.junit.Test

internal class GetRandomDiceUseCaseTest {
    @Test
    fun `check if dices roll result is enough random`() {

        val testQuantity = 1000
        val min = (testQuantity * 0.7)
        val max = (testQuantity * 0.9)
        val faces = 7 // for 6 faces use 7
        val randNumList = ArrayList<Int>()

        for (i in 1..testQuantity) {
            val a = GetRandomDiceUseCase().invoke(faces, 5)
            for (item in a) {
                randNumList.add(item)
            }
        }

        val rank = List(faces - 1) { 0 }.toMutableList()

        for (i in randNumList.indices) {
            rank[randNumList[i] - 1] = rank[randNumList[i] - 1] + 1
        }
        println("\n   min=${min.toInt()} <--> max=${max.toInt()}")
        for (i in rank.indices) {
            println("* DICE SIDE ${i + 1}: ${rank[i]} times. *")
            TestCase.assertTrue(rank[i] in min.toInt()..max.toInt())
        }
    }
}