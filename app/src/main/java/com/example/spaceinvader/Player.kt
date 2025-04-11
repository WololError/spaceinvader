package com.example.spaceinvader

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF

class Player private constructor(
    x: Float,
    y: Float
) : Entity(x, y, 200f, 60f, 0f, 0f) {


    private var Head = Paint()
    var weapon = Weapon()

    init {
        paint.color = Color.GREEN
        Head.color = Color.GREEN
    }

    override fun draw(canvas: Canvas) {
        canvas.drawRect(body, paint)

        val size = 20f
        val offsetX = 8f
        val centerX = body.centerX() + offsetX
        val topY = body.top

        val frontRect = RectF(
            centerX - size / 2f,
            topY - size,
            centerX + size / 2f,
            topY
        )
        canvas.drawRect(frontRect, Head)
    }

    fun clampToScreen(screenWidth: Int) {
        if (body.left < 0f) {
            body.offsetTo(0f, body.top)
        }
        if (body.right > screenWidth) {
            body.offsetTo(screenWidth - body.width(), body.top)
        }
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
