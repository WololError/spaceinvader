package com.example.spaceinvader

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF

class Player private constructor(x: Float, y: Float)
    : Entity(x, y, 200f, 60f, 0f, 0f) {


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

    fun restrictMovementToEdges(leftEdge: Edge, rightEdge: Edge) {
        if (body.left < leftEdge.body.right) {
            body.offsetTo(leftEdge.body.right, body.top)
        }
        if (body.right > rightEdge.body.left) {
            body.offsetTo(rightEdge.body.left - body.width(), body.top)
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
