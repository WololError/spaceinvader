package com.example.spaceinvader

abstract class Entity(
    var x: Float,
    var y: Float,
    var width: Float,
    var height: Float,
    var speedX: Float,
    var speedY: Float
) : Movable {
}
