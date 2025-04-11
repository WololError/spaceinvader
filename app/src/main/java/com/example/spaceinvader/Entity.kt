package com.example.spaceinvader

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF

abstract class Entity(
    var x: Float,
    var y: Float,
    var width: Float,
    var height: Float,
    var speedX: Float,
    var speedY: Float,
) : Movable {
    var body: RectF = RectF(x, y, x + width, y + height)
    var paint: Paint = Paint()

    override fun move() {
        body.offset(speedX, speedY)
    }
    override fun draw(canvas: Canvas) {
        canvas.drawRect(body, paint)
    }
}
