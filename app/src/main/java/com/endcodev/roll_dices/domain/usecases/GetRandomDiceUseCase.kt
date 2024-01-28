package com.endcodev.roll_dices.domain.usecases

import com.endcodev.roll_dices.data.repository.RandomRepository

class GetRandomDiceUseCase {

    operator fun invoke(faces: Int, diceQuantity: Int): List<Int> {
        return RandomRepository().invoke(faces, diceQuantity)
    }
}