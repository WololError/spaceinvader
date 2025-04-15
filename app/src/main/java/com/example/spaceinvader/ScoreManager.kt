package com.example.spaceinvader

class ScoreManager(val MaxScore: Int, private val endGameCallback: () -> Unit) {

    private var score: Int = 0

    fun update() {
        score += 1
        if (score == MaxScore) {
            endGame { endGameCallback() }
        }

    }

    private fun endGame(onWin: () -> Unit) {
        onWin()
    }

    fun reset() {
        score = 0
    }

    fun getScore(): Int {
        return score
    }

}
