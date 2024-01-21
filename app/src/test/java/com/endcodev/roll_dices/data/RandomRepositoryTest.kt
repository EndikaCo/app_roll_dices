package com.endcodev.roll_dices.data


import com.endcodev.roll_dices.data.repository.RandomRepository
import junit.framework.TestCase
import org.junit.Test

internal class RandomRepositoryTest {

    @Test
    fun invoke_should_return_a_list_of_integers_with_correct_size() {
        val result = RandomRepository().invoke(6, 3)
        TestCase.assertEquals(3, result.size)
    }

    @Test
    fun invoke_should_return_a_list_of_integers_between_1_and_number_of_faces() {
        val result = RandomRepository().invoke(6, 3)
        for (item in result) {
            TestCase.assertTrue(item in 1..6)
        }
    }
}