package com.example.spaceinvader

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import java.util.Random

class Player(x: Float, y: Float) : Entity(x, y, 200f, 60f, 0f, 0f), Movable {
    val r = RectF(x, y, x + width, y + height)
    val paint = Paint()
    val random = Random()

    init {
        paint.color = Color.GREEN
    }

    fun draw(canvas: Canvas) {
        canvas.drawRect(r, paint)
    }

    override fun move() {
        r.offset(speedX, speedY)
    }
}