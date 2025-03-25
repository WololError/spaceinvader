package com.example.spaceinvader

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF

class Bottom(x: Float, y: Float) : Border(
    body = RectF(x, y, x + 1700f, y + 100f),
    paint = Paint().apply {
        color = Color.argb(60, 255, 255, 255)
    }
)

