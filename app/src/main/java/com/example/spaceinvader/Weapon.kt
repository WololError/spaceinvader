package com.example.spaceinvader

import android.content.Context
import android.media.MediaPlayer
import com.example.spaceinvader.R

class Weapon(context: Context) {
    val damage: Int = 1
    private val shootSound = MediaPlayer.create(context, R.raw.shoot)

    fun shoot(x: Float, y: Float): Bullet {
        val bullet = Bullet(x, y)
        bullet.damage = damage
        shootSound.start()
        return bullet
    }

}