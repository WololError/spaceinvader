package com.example.spaceinvader

class Bullet(
    x: Float,
    y: Float,
    val damage: Int
) : Entity(x, y, width = 10f, height = 20f, speedX = 0f, speedY = -15f), Movable {

    override fun move() {
        // Implement the movement logic for the bullet
        x += speedX
        y += speedY
    }
}