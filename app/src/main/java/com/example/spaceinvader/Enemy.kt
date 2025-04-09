package com.example.spaceinvader

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Path

class Enemy(x: Float, y: Float, var health: Int, val bottom: Bottom) : Entity(
    x, y,
    60f, 60f,
    listOf(-5f, 5f).random(),
    8f
), Movable {
    val triangle = RectF(x, y, x + width, y + height)
    val paint = Paint()

    init {
        when (this.health) {
            1 -> paint.color = Color.RED
            2 -> paint.color = Color.rgb(142, 68, 173) // PURPLE
            3 -> paint.color = Color.rgb(255, 0, 255) // PINK
        }
    }

    fun draw(canvas: Canvas) {
        val path = Path()
        path.moveTo(triangle.left, triangle.top)
        path.lineTo(triangle.right, triangle.top)
        path.lineTo(triangle.centerX(), triangle.bottom)
        path.close()

        canvas.drawPath(path, paint)
    }

    override fun move() {
        triangle.offset(speedX, speedY)
    }

    fun bounceX() {
        speedX *= -1
    }

    fun isTouchingAnOtherEnnemy(other: Enemy): Boolean {
        return RectF.intersects(this.triangle, other.triangle)
    }

    fun takeDamage(bullet: Bullet) {
        if (health - bullet.damage <= 0) {
            health = 0
        } else {
            health = health - bullet.damage
        }
    }

    fun isTouchingBottom(): Boolean {
        return RectF.intersects(this.triangle, bottom.body)
    }

    fun isFacingPlayer(player: Player): Boolean {
        return (this.triangle.left < player.Body.right && this.triangle.right > player.Body.left)
    }
}