package com.example.spaceinvader

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Path

class Enemy(x: Float, y: Float, val health: Int) : Entity(x, y, 60f, 60f, 0f, 5f), Movable {

    val r = RectF(x, y, x + width, y + height)
    val paint = Paint()

    init {
        paint.color = Color.RED
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

    fun updateDirection(newSpeedX: Float, newSpeedY: Float) {
        speedX = newSpeedX
        speedY = newSpeedY
    }
}