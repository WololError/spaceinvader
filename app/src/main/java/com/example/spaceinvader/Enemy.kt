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
    6f
) {

    init {
        when (this.health) {
            1 -> paint.color = Color.RED
            2 -> paint.color = Color.rgb(142, 68, 173) // PURPLE
            3 -> paint.color = Color.rgb(255, 0, 255) // PINK
        }
    }

    override fun draw(canvas: Canvas) {
        val path = Path()
        path.moveTo(body.left, body.top)
        path.lineTo(body.right, body.top)
        path.lineTo(body.centerX(), body.bottom)
        path.close()

        canvas.drawPath(path, paint)
    }


    fun bounceX() {
        speedX *= -1
    }

    fun isTouchingAnOtherEnnemy(other: Enemy): Boolean {
        return RectF.intersects(this.body, other.body)
    }

    fun takeDamage(bullet: Bullet) {
        if (health - bullet.damage <= 0) {
            health = 0
        } else {
            health = health - bullet.damage
        }
    }

    fun isTouchingBottom(): Boolean {
        return RectF.intersects(this.body, bottom.body)
    }

    fun isHitBy(bullet: Bullet): Boolean {
        return RectF.intersects(this.body, bullet.body)
    }

    fun bounceIfTouchingBorders(leftEdge: Edge, rightEdge: Edge) {
        if (RectF.intersects(this.body, leftEdge.body) || RectF.intersects(this.body, rightEdge.body)) {
            bounceX()
        }
    }


}