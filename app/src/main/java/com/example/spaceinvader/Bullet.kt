package com.example.spaceinvader

import android.graphics.Color

class Bullet(x: Float, y: Float) : Entity(x, y, 10f, 40f, 0f, -20f) {
    var damage: Int? = null

    init {
        paint.color = Color.YELLOW
    }

}