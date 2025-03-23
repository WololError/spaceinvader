package com.example.spaceinvader

import android.content.Context
import android.graphics.Canvas
import android.view.MotionEvent
import android.view.SurfaceView

class MyView(context: Context) : SurfaceView(context) {
    private lateinit var player: Player
    private var isMovingLeft = false
    private var isMovingRight = false
    private val enemies = mutableListOf<Enemy>()

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        player = Player(w / 2f - 100, 1700f)

        val enemyWidth = 60f
        val enemyHeight = 60f
        val numRows = 3
        val numCols = 5
        val spacing = 100f
        val xOffset = 200f
        val yOffset = 100f

        for (row in 0 until numRows) {
            for (col in 0 until numCols) {
                val x = col * (enemyWidth + spacing) + xOffset
                val y = row * (enemyHeight + spacing) + yOffset
                enemies.add(Enemy(x, y, 100))
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                if (event.x < measuredWidth / 2f) {
                    isMovingLeft = true
                    isMovingRight = false
                } else {
                    isMovingLeft = false
                    isMovingRight = true
                }
            }
            MotionEvent.ACTION_UP -> {
                isMovingLeft = false
                isMovingRight = false
            }
        }
        return true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (isMovingLeft) {
            player.speedX = -10f
        } else if (isMovingRight) {
            player.speedX = 10f
        } else {
            player.speedX = 0f
        }
        player.move()
        player.draw(canvas)

        for (enemy in enemies) {
            enemy.draw(canvas)
        }
        invalidate()
    }
}