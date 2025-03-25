package com.example.spaceinvader

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF

abstract class Border(protected val body: RectF, protected val paint: Paint) {

    fun draw(canvas: Canvas) {
        canvas.drawRect(body, paint)
    }

    fun isTouchedByThisEnemy(enemy: Enemy): Boolean {
        return RectF.intersects(body, enemy.r)
    }

    fun isTouchedByThePlayer(player: Player): Boolean {
        return RectF.intersects(body, player.Body)
    }
}
