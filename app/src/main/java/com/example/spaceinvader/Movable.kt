package com.example.spaceinvader

import android.graphics.Canvas

interface Movable {
    fun move()
    fun draw(canvas: Canvas)
}
