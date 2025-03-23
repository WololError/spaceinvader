package com.example.spaceinvader


import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF

class Bottom(x: Float, y: Float) {

    private val width: Float = 1700f
    private val height: Float = 100f

    val r = RectF(x, y, x + width, y + height)

    private val paint = Paint().apply {
        color = Color.argb(60, 255, 255, 255) // blanc semi-transparent
    }

    fun draw(canvas: Canvas) {
        canvas.drawRect(r, paint)
    }

    fun isTouchedBy(enemy: Enemy): Boolean {
        return RectF.intersects(r, enemy.r)
    }
}