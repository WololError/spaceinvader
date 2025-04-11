package com.example.spaceinvader

class ScoreManager  {
    var score: Int = 0

    fun EnemyKilled() {
        score += 1
    }
}