package com.example.spaceinvader

class Weapon(val damage: Int = 1, val fireRate: Float = 1.0f) {

    fun fire(x: Float, y: Float): Bullet {
        val bullet = Bullet(x, y)
        bullet.damage = damage
        bullet.move()
        return bullet
    }
}
