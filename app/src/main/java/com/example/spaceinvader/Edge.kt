package com.example.spaceinvader

import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF

class Edge(x: Float, y: Float) : Border(
    body = RectF(x, y, x + 50f, y + 2500f),
    paint = Paint().apply {
        color = Color.argb(80, 255, 255, 255)
    }
)
