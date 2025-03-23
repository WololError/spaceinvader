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
    var killedEnemies = 0
    private var NumberofEnemies = 0
    private val restartHandler = Handler(Looper.getMainLooper())
    private var gameOver = false
    private var enemyMovementRunnable: Runnable? = null
    private var gameEnded = false



    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        player = Player(w / 2f - 100, 1900f)
        bottom = Bottom(w / 2f - 100, 2000f)

        val enemyWidth = 60f
        val enemyHeight = 60f
        val numRows = 2
        val numCols = 4
        NumberofEnemies = numRows * numCols
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
        direction = 0 // reset direction

        // supprime l'ancien handler si relance
        enemyMovementRunnable?.let { handler.removeCallbacks(it) }

        enemyMovementRunnable = object : Runnable {
            override fun run() {
                when (direction) {
                    0 -> updateEnemyDirection(0f, 5f)
                    1 -> updateEnemyDirection(5f, 5f)
                    2 -> updateEnemyDirection(0f, 5f)
                    3 -> updateEnemyDirection(-5f, 5f)
                }
                direction = (direction + 1) % 4
                handler.postDelayed(this, 1000)
            }
        }

        handler.postDelayed(enemyMovementRunnable!!, 250)
    }

    private fun updateEnemyDirection(speedX: Float, speedY: Float) {
        for (enemy in enemies) {
            enemy.updateDirection(speedX, speedY)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN, MotionEvent.ACTION_MOVE -> {
                isMovingLeft = false
                isMovingRight = false

                for (i in 0 until event.pointerCount) {
                    val x = event.getX(i)
                    val y = event.getY(i)

                    val leftZone = x < measuredWidth / 2f - 100 && y > 2000
                    val rightZone = x > measuredWidth / 2f + 100 && y > 2000
                    val shootZone = !(leftZone || rightZone)

                    if (leftZone) {
                        isMovingLeft = true
                    } else if (rightZone) {
                        isMovingRight = true
                    }

                    if (shootZone && canShoot) {
                        val bullet = player.weapon.fire(player.Body.centerX(), player.Body.top)
                        bullets.add(bullet)
                        canShoot = false
                    }
                }
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP -> {
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
                if (!gameOver) {   // ← évite de le déclencher plusieurs fois
                    gameOver = true
                    enemies.clear()
                    showGameOver(context.getString(R.string.lose)) // ← directement ici
                }
                break // ← on arrête la boucle
            }
        }


        checkCollisions()
        invalidate()
        if (killedEnemies == NumberofEnemies && !gameOver && !gameEnded) {
            gameOver = true
            showGameOver(context.getString(R.string.win))
            return
        }



    }
    private fun showGameOver(message: String) {
        gameOver = false
        gameEnded = true

        (context as? androidx.fragment.app.FragmentActivity)?.let { activity ->
            GameOverDialog(message) {
                restartGame()
            }.show(activity.supportFragmentManager, "GameOver")
        }
    }

    private fun restartGame() {
        gameEnded = false
        enemies.clear()
        bullets.clear()
        killedEnemies = 0
        gameOver = false
        direction = 0 // reset movement direction

        // recréer le joueur et le fond si nécessaire (optionnel si toujours valide)
        player = Player(width / 2f - 100, 1900f)
        bottom = Bottom(width / 2f - 100, 2000f)

        val enemyWidth = 60f
        val enemyHeight = 60f
        val numRows = 2
        val numCols = 4
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
                    killedEnemies = killedEnemies + 1
                    break
                }
            }
        }
    }

}