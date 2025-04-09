package com.example.spaceinvader

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF

class Player private constructor(
    x: Float,
    y: Float
) : Entity(x, y, 200f, 60f, 0f, 0f), Movable {

    var Body = RectF(x, y, x + width, y + height)
    private var paint = Paint()
    private var Head = Paint()
    var weapon = Weapon()

    init {
        paint.color = Color.GREEN
        Head.color = Color.GREEN
    }

    fun draw(canvas: Canvas) {
        canvas.drawRect(Body, paint)

        val size = 20f
        val offsetX = 8f
        val centerX = Body.centerX() + offsetX
        val topY = Body.top

        val frontRect = RectF(
            centerX - size / 2f,
            topY - size,
            centerX + size / 2f,
            topY
        )
        canvas.drawRect(frontRect, Head)
    }

    override fun move() {
        Body.offset(speedX, speedY)
    }

    companion object {
        private var INSTANCE: Player? = null

        fun getInstance(x: Float, y: Float): Player {
            if (INSTANCE == null) {
                INSTANCE = Player(x, y)
            }
            return INSTANCE!!
        }
        
        fun resetSingleton() {
            INSTANCE = null
        }
    }
}
