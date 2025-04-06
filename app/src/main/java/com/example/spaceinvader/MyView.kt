package com.example.spaceinvader

import android.content.Context
import android.graphics.Canvas
import android.graphics.RectF
import android.view.MotionEvent
import android.view.SurfaceView
import android.media.MediaPlayer

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

    private var killedEnemies = 0
    private var numberOfEnemies = 0
    private var gameOver = false
    private var gameEnded = false

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        // On récupère l'instance unique du Player
        player = Player.getInstance(w / 2f - 100, 1900f)

        bottom = Bottom(w / 2f - 100, 2000f)
        leftEdge = Edge(0f, 0f)
        rightEdge = Edge(width - 50f, 0f)

        val enemyWidth = 60f
        val enemyHeight = 60f
        val numRows = 2
        val numCols = 2
        numberOfEnemies = numRows * numCols
        val spacing = 100f
        val xOffset = 100f
        val yOffset = 0f

        for (row in 0 until numRows) {
            for (col in 0 until numCols) {
                val x = col * (enemyWidth + spacing) + xOffset
                val y = row * (enemyHeight + spacing) + yOffset
                val enemy = Enemy(x, y, (1..3).random(), bottom)
                enemies.add(enemy)
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN,
            MotionEvent.ACTION_POINTER_DOWN,
            MotionEvent.ACTION_MOVE -> {

                isMovingLeft = false
                isMovingRight = false

                for (i in 0 until event.pointerCount) {
                    val x = event.getX(i)
                    val y = event.getY(i)

                    val leftZone = x < measuredWidth / 2f - 100 && y > 1800
                    val rightZone = x > measuredWidth / 2f + 100 && y > 1800
                    val shootZone = !(leftZone || rightZone)

                    if (leftZone) isMovingLeft = true
                    if (rightZone) isMovingRight = true

                    if (shootZone && canShoot) {
                        // On crée un bullet via l'arme du joueur
                        val bullet = player.weapon.fire(player.Body.centerX(), player.Body.top)
                        bullets.add(bullet)
                        canShoot = false
                        shootSound.start()
                    }
                }
            }

            MotionEvent.ACTION_UP,
            MotionEvent.ACTION_POINTER_UP -> {
                isMovingLeft = false
                isMovingRight = false
                canShoot = true
            }
        }
        return true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Gestion du joueur
        player.speedX = when {
            isMovingLeft -> -10f
            isMovingRight -> 10f
            else -> 0f
        }
        player.move()

        // Collision avec les bords pour le joueur
        if (player.Body.left < 0f) {
            player.Body.offsetTo(0f, player.Body.top)
        }
        if (player.Body.right > width) {
            player.Body.offsetTo(width - player.Body.width(), player.Body.top)
        }

        // Dessin du joueur
        player.draw(canvas)

        // Bullets
        val bulletIterator = bullets.iterator()
        while (bulletIterator.hasNext()) {
            val bullet = bulletIterator.next()
            bullet.move()
            bullet.draw(canvas)
            if (bullet.y + bullet.height < 0) {
                bulletIterator.remove()
            }
        }

        // Enemies
        for (enemy in enemies) {
            enemy.move()

            // Si un ennemi touche le bas ou dépasse le joueur => game over
            if (enemy.isTouchingBottom() || enemy.y > player.y) {
                if (!gameOver) {
                    gameOver = true
                    enemies.clear()
                    showGameOver(context.getString(R.string.lose))
                }
                break
            }

            // Rebond sur les bords
            if (enemy.triangle.left <= 0f || enemy.triangle.right >= width) {
                enemy.bounceX()
            }

            // Dessin
            enemy.draw(canvas)

            // Collision entre ennemis eux-mêmes
            for (i in enemies.indices) {
                for (j in i + 1 until enemies.size) {
                    if (enemies[i].isTouchingAnOtherEnnemy(enemies[j])) {
                        enemies[i].bounceX()
                        enemies[j].bounceX()
                    }
                }
            }
        }

        checkCollisionsBetweenBulletAndEnnemy()

        // On redessine en continu
        invalidate()

        // Victoire si tous les ennemis sont morts
        if (killedEnemies == numberOfEnemies && !gameOver && !gameEnded) {
            gameOver = true
            showGameOver(context.getString(R.string.win))
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
        gameOver = false
        killedEnemies = 0
        bullets.clear()
        enemies.clear()

        onSizeChanged(width, height, width, height)
    }

    private fun checkCollisionsBetweenBulletAndEnnemy() {
        val bulletIterator = bullets.iterator()
        while (bulletIterator.hasNext()) {
            val bullet = bulletIterator.next()
            val enemyIterator = enemies.iterator()

            while (enemyIterator.hasNext()) {
                val enemy = enemyIterator.next()
                if (RectF.intersects(bullet.r, enemy.triangle)) {
                    // Impact
                    enemy.takeDamage(bullet)

                    if (enemy.health == 0) {
                        enemyIterator.remove()  // Ennemi mort, on le retire
                        killedEnemies++
                        enemyDeathSound.start()
                    }
                    bulletIterator.remove()  // La balle est consommée
                    break // On sort de la boucle, balle déjà utilisée
                }
            }
        }
    }
}