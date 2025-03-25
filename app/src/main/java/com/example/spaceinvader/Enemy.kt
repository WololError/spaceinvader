package com.example.spaceinvader

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Path
import kotlin.random.Random

class Enemy(x: Float, y: Float, var health: Int) : Entity(
    x, y,
    60f, 60f,
    listOf(-5f, 5f).random(),
    3f
), Movable {
    val r = RectF(x, y, x + width, y + height)
    val paint = Paint()

    init {
        when (this.health) {
            1 -> paint.color = Color.RED
            2 -> paint.color = Color.rgb(128, 0, 128) // PURPLE
            3 -> paint.color = Color.rgb(255, 192, 203) // PINK
        }
    }

    fun draw(canvas: Canvas) {
        val path = Path()
        path.moveTo(r.left, r.top)
        path.lineTo(r.right, r.top)
        path.lineTo(r.centerX(), r.bottom)
        path.close()

        canvas.drawPath(path, paint)
    }

    override fun move() {
        r.offset(speedX, speedY)
    }

    fun bounceX() {
        speedX *= -1
    }
    fun isTouchingAnOtherEnnemy(other: Enemy): Boolean {
        return RectF.intersects(this.r, other.r)


    }
    fun takeDamage() {
        if (health - 1 <= 0) {
            health = 0
        } else {
            health -= 1
        }
    }
    fun isTouchingBottom(bottom: Bottom): Boolean {
        return RectF.intersects(this.r, bottom.body)
    }

}
