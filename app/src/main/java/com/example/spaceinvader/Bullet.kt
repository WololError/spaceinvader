package com.example.spaceinvader

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF

class Bullet(x: Float, y: Float) : Entity(x, y, 10f, 40f, 0f, -20f), Movable {
    var damage : Int = 0
    val r = RectF(x, y, x + width, y + height)
    val paint = Paint()

    init {
        paint.color = Color.YELLOW
    }

    fun draw(canvas: Canvas) {
        canvas.drawRect(r, paint)
    }

    override fun move() {
        r.offset(speedX, speedY)
    }
}