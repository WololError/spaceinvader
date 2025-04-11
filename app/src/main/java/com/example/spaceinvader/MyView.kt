package com.example.spaceinvader

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import android.view.SurfaceView
import android.os.Handler
import android.os.Looper
import android.media.MediaPlayer
import kotlin.random.Random

class MyView(context: Context) : SurfaceView(context) {

    private lateinit var leftEdge: Edge
    private lateinit var rightEdge: Edge
    private lateinit var bottom: Bottom
    private lateinit var player: Player

    private val bullets = mutableListOf<Bullet>()
    private val enemies = mutableListOf<Enemy>()

    private val enemyDeathSound = MediaPlayer.create(context, R.raw.enemydeath)
    private val shootSound = MediaPlayer.create(context, R.raw.shoot)

    private var isMovingLeft = false
    private var isMovingRight = false
    private var canShoot = true

    private var numberOfEnemies = 0
    private var gameOver = false
    private var gameEnded = false
    private var isGameStarted = false
    private var isEndlessMode = false

    private val spawnHandler = Handler(Looper.getMainLooper())
    private var spawnRunnable: Runnable? = null

    private val scoreManager = ScoreManager()

    init {
        showStartDialog()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        if (!isGameStarted) return

        player = Player.getInstance(w / 2f - 100, 1900f)
        bottom = Bottom(w / 2f - 100, 2000f)
        leftEdge = Edge(0f, 0f)
        rightEdge = Edge(width - 50f, 0f)

        if (!isEndlessMode) {
            setupFixedEnemies()
        }
    }

    private fun setupFixedEnemies() {
        enemies.clear()
        val enemyWidth = 60f
        val enemyHeight = 60f
        val numRows = 2
        val numCols = 2
        numberOfEnemies = numRows * numCols
        val spacing = 100f
        val xOffset = 100f

        for (row in 0 until numRows) {
            for (col in 0 until numCols) {
                val x = col * (enemyWidth + spacing) + xOffset
                val y = row * (enemyHeight + spacing)
                enemies.add(Enemy(x, y, (1..3).random(), bottom))
            }
        }
    }

    private fun showStartDialog() {
        (context as? androidx.fragment.app.FragmentActivity)?.let { activity ->
            StartDialog(
                onStartNormalMode = {
                    isGameStarted = true
                    isEndlessMode = false
                    restartGame()
                    requestLayout()
                    invalidate()
                },
                onStartEndlessMode = {
                    isGameStarted = true
                    isEndlessMode = true
                    restartGame()
                    startEndlessSpawning()
                    requestLayout()
                    invalidate()
                }
            ).show(activity.supportFragmentManager, "StartDialog")
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!isGameStarted) return true

        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN, MotionEvent.ACTION_MOVE -> {

                isMovingLeft = false
                isMovingRight = false

                var shootingDetected = false

                for (i in 0 until event.pointerCount) {
                    val x = event.getX(i)
                    val y = event.getY(i)

                    val leftZone = x < measuredWidth / 2f - 100 && y > 1800
                    val rightZone = x > measuredWidth / 2f + 100 && y > 1800
                    val shootZone = !(leftZone || rightZone)

                    if (leftZone) isMovingLeft = true
                    if (rightZone) isMovingRight = true

                    if (shootZone && canShoot && !shootingDetected) {
                        val bullet = player.weapon.fire(player.body.centerX(), player.body.top)
                        bullets.add(bullet)
                        canShoot = false
                        shootSound.start()
                        shootingDetected = true
                    }
                }
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP -> {


                isMovingLeft = false
                isMovingRight = false

                val activePointerCount = event.pointerCount - 1

                for (i in 0 until activePointerCount) {
                    val x = event.getX(i)
                    val y = event.getY(i)

                    val leftZone = x < measuredWidth / 2f - 100 && y > 1800
                    val rightZone = x > measuredWidth / 2f + 100 && y > 1800

                    if (leftZone) isMovingLeft = true
                    if (rightZone) isMovingRight = true
                }

                canShoot = true
            }
        }

        return true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (!isGameStarted) return

        updatePlayer(canvas)
        updateBullets(canvas)
        updateEnemies(canvas)
        checkVictoryCondition()
        drawScore(canvas)

        invalidate()
    }

    private fun updatePlayer(canvas: Canvas) {
        player.speedX = when {
            isMovingLeft -> -10f
            isMovingRight -> 10f
            else -> 0f
        }
        player.move()
        player.clampToEdges(leftEdge, rightEdge)
        player.draw(canvas)
    }

    private fun updateBullets(canvas: Canvas) {
        val bulletIterator = bullets.iterator()
        while (bulletIterator.hasNext()) {
            val bullet = bulletIterator.next()
            bullet.move()
            bullet.draw(canvas)
            if (bullet.y + bullet.height < 0) bulletIterator.remove()
        }
    }

    private fun updateEnemies(canvas: Canvas) {
        val enemyIterator = enemies.iterator()
        while (enemyIterator.hasNext()) {
            val enemy = enemyIterator.next()
            enemy.move()

            if (enemy.isTouchingBottom() || enemy.y > player.y) {
                if (!gameOver) {
                    gameOver = true
                    enemies.clear()
                    stopEndlessSpawning()
                    showGameOver(context.getString(R.string.lose))
                }
                break
            }

            enemy.bounceIfTouchingBorders(leftEdge, rightEdge)
            enemy.draw(canvas)
        }
        checkCollisionsBetweenEnemies()
        checkCollisionsBetweenBulletAndEnemy()
    }

    private fun checkVictoryCondition() {
        if (!isEndlessMode && scoreManager.score == numberOfEnemies && !gameOver && !gameEnded) {
            gameOver = true
            showGameOver(context.getString(R.string.win))
        }
    }

    private fun drawScore(canvas: Canvas) {
        val paint = Paint().apply {
            color = Color.WHITE
            textSize = 60f
        }
        canvas.drawText("Score: ${scoreManager.score}", 50f, 100f, paint)
    }

    private fun showGameOver(message: String) {
        gameOver = false
        gameEnded = true
        isGameStarted = false
        stopEndlessSpawning()

        (context as? androidx.fragment.app.FragmentActivity)?.let { activity ->
            var finalMessage = message

            if (isEndlessMode) {
                finalMessage += "\nEnnemis tués : ${scoreManager.score}"
            } else {
                finalMessage += "\n "
            }

            GameOverDialog(finalMessage) {
                restartGame()
                showStartDialog()
            }.show(activity.supportFragmentManager, "GameOver")
        }
    }

    private fun restartGame() {
        gameEnded = false
        gameOver = false
        scoreManager.score = 0
        bullets.clear()
        enemies.clear()
        onSizeChanged(width, height, width, height)
    }

    private fun checkCollisionsBetweenBulletAndEnemy() {
        val bulletIterator = bullets.iterator()
        while (bulletIterator.hasNext()) {
            val bullet = bulletIterator.next()
            val enemyIterator = enemies.iterator()

            while (enemyIterator.hasNext()) {
                val enemy = enemyIterator.next()
                if (enemy.isHitBy(bullet)) {
                    enemy.takeDamage(bullet)

                    if (enemy.health == 0) {
                        enemyIterator.remove()
                        scoreManager.EnemyKilled()
                        enemyDeathSound.start()
                    }
                    bulletIterator.remove()
                    break
                }
            }
        }
    }

    private fun checkCollisionsBetweenEnemies() {
        for (i in enemies.indices) {
            for (j in i + 1 until enemies.size) {
                if (enemies[i].isTouchingAnOtherEnnemy(enemies[j])) {
                    enemies[i].bounceX()
                    enemies[j].bounceX()
                }
            }
        }
    }

    private fun startEndlessSpawning() {
        spawnRunnable = object : Runnable {
            override fun run() {
                if (!gameOver) {
                    spawnEnemy()
                    spawnHandler.postDelayed(this, 1000)
                }
            }
        }
        spawnHandler.post(spawnRunnable!!)
    }

    private fun stopEndlessSpawning() {
        spawnRunnable?.let { spawnHandler.removeCallbacks(it) }
    }

    private fun spawnEnemy() {
        val x = Random.nextFloat() * (width - 60f)
        val enemy = Enemy(x, 0f, (1..3).random(), bottom)
        enemies.add(enemy)
        numberOfEnemies++
    }
}