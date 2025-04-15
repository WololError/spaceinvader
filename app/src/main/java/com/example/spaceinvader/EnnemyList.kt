package com.example.spaceinvader

class EnnemyList(val numberOfEnemies: Int, val endGameCallback: () -> Unit) {

    private val thelist: MutableList<Enemy> = mutableListOf()
    private val observer = ScoreManager(numberOfEnemies, endGameCallback)

    fun addEnemy(enemy: Enemy) {
        thelist.add(enemy)
    }

    fun removeEnemy(enemy: Enemy) {
        thelist.remove(enemy)
        notifyObserver()
    }

    fun getInterator(): MutableIterator<Enemy> {
        return thelist.iterator()
    }

    fun clearEnemies() {
        thelist.clear()
    }

    private fun notifyObserver() {
        observer.update()
    }

    fun resetobserver() {
        observer.reset()
    }

    fun NumberOfEnnemiesKilldes(): Int {
        return observer.getScore()
    }

    fun getthelist(): MutableList<Enemy> {
        return thelist
    }
}