package com.example.spaceinvader

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF

class Player private constructor(context: Context, x: Float, y: Float)
    : Entity(x, y, 200f, 60f, 0f, 0f) {

    private var Head = Paint()
    private var weapon = Weapon(context)

    init {
        paint.color = Color.GREEN
        Head.color = Color.GREEN
    }

    fun restrictMovementToEdges(leftEdge: Edge, rightEdge: Edge) {
        if (body.left < leftEdge.body.right) {
            body.offsetTo(leftEdge.body.right, body.top)
        }
        if (body.right > rightEdge.body.left) {
            body.offsetTo(rightEdge.body.left - body.width(), body.top)
        }
    }

    fun shoot(): Bullet {
        return weapon.shoot(body.centerX(), body.top)
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

    companion object {
        private var INSTANCE: Player? = null

        fun getInstance(context: Context, x: Float, y: Float): Player {
            if (INSTANCE == null) {
                INSTANCE = Player(context, x, y)
            }
            return INSTANCE!!
        }

        fun resetSingleton() {
            INSTANCE = null
        }
    }
}

