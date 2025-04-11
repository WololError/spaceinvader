package com.example.spaceinvader

import android.graphics.Color

class Bullet(x: Float, y: Float) : Entity(x, y, 10f, 40f, 0f, -20f) {
    var damage : Int = 0

    init {
        paint.color = Color.YELLOW
    }

}