package com.example.spaceinvader

import android.content.Context
import android.media.MediaPlayer

class Weapon(val damage: Int = 1) {

    fun fire(x: Float, y: Float, context: Context): Bullet {
        val bullet = Bullet(x, y)
        bullet.damage = damage
        bullet.move()

        val shootSound = MediaPlayer.create(context, R.raw.shoot)
        shootSound.start()

        return bullet
    }
}
