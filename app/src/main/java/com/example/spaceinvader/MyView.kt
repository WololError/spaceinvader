package com.example.spaceinvader

import android.content.Context
import android.graphics.Canvas
import android.graphics.RectF
import android.view.MotionEvent
import android.view.SurfaceView
import android.os.Handler
import android.os.Looper
import android.widget.Toast

class MyView(context: Context) : SurfaceView(context) {

    private lateinit var player: Player
    private lateinit var bottom: Bottom
    var isMovingLeft = false
    var isMovingRight = false
    var canShoot = true
    val bullets = mutableListOf<Bullet>()
    val enemies = mutableListOf<Enemy>()
    private val handler = Handler(Looper.getMainLooper())
    private var direction = 0

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        player = Player(w / 2f - 100, 1900f)
        bottom = Bottom(w / 2f - 100, 2000f)

        val enemyWidth = 60f
        val enemyHeight = 60f
        val numRows = 2
        val numCols = 5
        val spacing = 100f
        val xOffset = 0f
        val yOffset = 0f

        for (row in 0 until numRows) {
            for (col in 0 until numCols) {
                val x = col * (enemyWidth + spacing) + xOffset
                val y = row * (enemyHeight + spacing) + yOffset
                enemies.add(Enemy(x, y, 100))
            }
        }

        startEnemyMovement()
    }

    private fun startEnemyMovement() {
        handler.postDelayed(object : Runnable {
            override fun run() {
                when (direction) {
                    0 -> updateEnemyDirection(0f, 5f) // Descend
                    1 -> updateEnemyDirection(5f, 5f) // Diagonal right-down
                    2 -> updateEnemyDirection(0f, 5f) // Descend
                    3 -> updateEnemyDirection(-5f, 5f) // Diagonal left-down
                }
                direction = (direction + 1) % 4
                handler.postDelayed(this, 1000)
            }
        }, 250)
    }

    private fun updateEnemyDirection(speedX: Float, speedY: Float) {
        for (enemy in enemies) {
            enemy.updateDirection(speedX, speedY)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                val leftZone = event.x < measuredWidth / 2f - 100 && event.y > 1700
                val rightZone = event.x > measuredWidth / 2f + 100 && event.y > 1700

                when {
                    leftZone -> {
                        isMovingLeft = true
                        isMovingRight = false
                    }
                    rightZone -> {
                        isMovingLeft = false
                        isMovingRight = true
                    }
                    else -> {
                        if (canShoot) {
                            val bullet = player.weapon.fire(player.Body.centerX(), player.Body.top)
                            bullets.add(bullet)
                            canShoot = false
                        }
                    }
                }
            }

            MotionEvent.ACTION_UP -> {
                isMovingLeft = false
                isMovingRight = false
                canShoot = true
            }
        }

        return true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        player.speedX = when {
            isMovingLeft -> -10f
            isMovingRight -> 10f
            else -> 0f
        }

        player.move()
        player.draw(canvas)

        val iterator = bullets.iterator()
        while (iterator.hasNext()) {
            val bullet = iterator.next()
            bullet.move()
            bullet.draw(canvas)

            if (bullet.y + bullet.height < 0) {
                iterator.remove()
            }
        }
        for (enemy in enemies) {
            enemy.move()
            enemy.draw(canvas)
            if (bottom.isTouchedBy(enemy)) {
                Toast.makeText(context, "Game Over", Toast.LENGTH_SHORT).show()
            }
        }
        checkCollisions()
        invalidate()
    }

    private fun checkCollisions() {
        val bulletIterator = bullets.iterator()
        while (bulletIterator.hasNext()) {
            val bullet = bulletIterator.next()
            val enemyIterator = enemies.iterator()
            while (enemyIterator.hasNext()) {
                val enemy = enemyIterator.next()
                if (RectF.intersects(bullet.r, enemy.r)) {
                    bulletIterator.remove()
                    enemyIterator.remove()
                    break
                }
            }
        }
    }
}