package com.example.spaceinvader

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF

class Enemy(x: Float, y: Float, val health: Int) : Entity(x, y, 60f, 60f, 0f, 5f), Movable {

    val r = RectF(x, y, x + width, y + height)
    val paint = Paint()

    init {
        paint.color = Color.RED
    }

    fun draw(canvas: Canvas) {
        canvas.drawRect(r, paint)
    }

    override fun move() {
        r.offset(speedX, speedY)
    }
}