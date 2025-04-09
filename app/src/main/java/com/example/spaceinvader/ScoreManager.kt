package com.example.spaceinvader

class ScoreManager : EnemyDeathObserver {
    var score: Int = 0

    override fun onEnemyKilled() {
        score += 1
    }
}
