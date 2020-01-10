package com.olsson.aoc2019

import java.io.File
import java.lang.UnsupportedOperationException
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan2

fun main() {
    val input = Utils.getFromResources("day10.txt").readLines().reduce{ a, b -> "$a|$b"}
        .split('|')
    val day10 = Day10(input)
    day10.findBestScanPos()
    day10.predictErasingOrder()
}

class Day10 (
    val input: List<String>
) {

    private val asteroids = build(input)
    private var bestPos = Pair(0, 0)

    fun findBestScanPos() {
        var bestPos = Pair(0, 0)
        var bestResult = 0

        for (asteroid in asteroids) {
            val x = asteroid.getX()
            val y = asteroid.getY()
            val result = checkInLos(x, y)
            if (result > bestResult) {
                bestPos = Pair(x.toInt(), y.toInt())
                bestResult = result
            }
        }
        this.bestPos = bestPos
        println("Best pos is $bestPos with a result of $bestResult")
    }

    fun predictErasingOrder() {

        val base = asteroids.first { asteroid ->
            asteroid.position == Pair(bestPos.first.toDouble(), bestPos.second.toDouble())
        }

        val sortedAsteroids = asteroids
            .filter { asteroid -> asteroid != base }
            .map{ asteroid -> calcAngleBetween(base, asteroid) }
            .sortedBy { asteroid -> asteroid.angle }
            .toCollection(mutableListOf())

        printOrder(sortedAsteroids, base)
    }

    private fun printOrder(sortedAsteroid: List<Asteroid>, base: Asteroid) {
        val pulverized = mutableSetOf<Pair<Double, Double>>()
        var nextAngle = -1.0
        while ( pulverized.size < sortedAsteroid.size) {
             nextAngle = sortedAsteroid
                .firstOrNull { asteroid -> asteroid.angle > nextAngle }
                ?.angle ?: -1.0

            val nextAsteroid = sortedAsteroid
                .filter { asteroid -> !pulverized.contains(asteroid.position) }
                .filter { asteroid -> equalWithMargin(nextAngle, asteroid.angle)  }
                .minBy { asteroid -> abs(base.getX() - asteroid.getX()) +
                    abs(base.getY() - asteroid.getY())}

            if (nextAsteroid == null) {
                println("No next asteroid, if not done then something went wrong")
                return
            }
            pulverized.add(nextAsteroid.position)
            println("Nr ${pulverized.size} to be destroyed is ${nextAsteroid.position}")
        }
    }

    private fun calcAngleBetween(base: Asteroid?, asteroid: Asteroid) : Asteroid {
        if (base == null) {
            return Asteroid(Pair(-1.0, -1.0), 361.0)
        }

        val rightOf = asteroid.getX() > base.getX()
        val above = asteroid.getY() < base.getY()
        val aboveRight = above && rightOf
        val aboveLeft = above && !rightOf
        val belowRight = !above && rightOf
        val belowLeft = !above && !rightOf

        val deltaY = asteroid.getY() - base.getY()
        val deltaX = asteroid.getX() - base.getX()

        if (deltaX == 0.0) {
            if (base.getY() > asteroid.getY()) {
                asteroid.angle = 0.0
            } else {
                asteroid.angle = 180.0
            }
            return asteroid
        }
        if (deltaY == 0.0) {
            if (base.getX() > asteroid.getX()) {
                asteroid.angle = 270.0
            } else {
                asteroid.angle = 90.0
            }
            return asteroid
        }

        val angle = atan2(deltaY, deltaX) * (180 / PI)

        when {
            aboveRight -> asteroid.angle = 90.0 + angle
            aboveLeft -> asteroid.angle = 420 + angle
            belowRight -> asteroid.angle = 180 - angle
            belowLeft -> asteroid.angle = 360 - angle
            else -> throw UnsupportedOperationException("The points have no relation on the plane: ${base.position} - ${asteroid.position}")
        }
        return asteroid
    }

    private fun checkInLos(x: Double, y: Double) : Int {
        return asteroids
            .filter { asteroid -> asteroid.position != Pair(x, y) }
            .filter { asteroid -> !asteroidBetween(x, y, asteroid.getX(), asteroid.getY()) }
            .count()
    }

    private fun asteroidBetween(x: Double, y: Double, toX: Double, toY: Double) : Boolean {
        val m : Double
        val c: Double
        val deltaY = y - toY
        val deltaX = x - toX

        if (deltaX == 0.0) {
            return verticalCheck(x, y, toY)
        }
        if (deltaY == 0.0) {
            return horizontalCheck(x, y, toX)
        }

        m = deltaY / deltaX
        c = y - (m * x)

        val xRange = getRange(x, toX)
        val yRange = getRange(y, toY)

        return asteroids
            .filter { asteroid -> asteroid.getX().toInt() in xRange }
            .filter { asteroid -> asteroid.getY().toInt() in yRange }
            .filter { asteroid -> equalWithMargin(asteroid.getY(), (m * asteroid.getX() + c)) }
            .any()
    }
    
    private fun verticalCheck(x: Double, y: Double, toY: Double) : Boolean {
        val yRange = getRange(y, toY)
        return asteroids
            .filter { asteroid -> asteroid.getX() == x }
            .filter { asteroid -> asteroid.getY().toInt() in yRange }
            .any()
    }

    private fun horizontalCheck(x: Double, y: Double, toX: Double) : Boolean {
        val xRange = getRange(x, toX)
        return asteroids
            .filter { asteroid -> asteroid.getY() == y }
            .filter { asteroid -> asteroid.getX().toInt() in xRange }
            .any()
    }

    private fun getRange(first: Double, second: Double) : IntRange {
        return if (first > second) {
            (second.toInt() + 1) until first.toInt()
        } else {
            (first.toInt() + 1) until second.toInt()
        }
    }

    private fun equalWithMargin(value: Double, other: Double) : Boolean {
        return value + 0.0000001 > other && value - 0.0000001 < other
    }

    private fun build(input: List<String>) : Set<Asteroid> {
        val asteroids = mutableSetOf<Asteroid>()
        input.forEachIndexed{ y, row ->
            row.forEachIndexed{ x, char ->
                if (char.toString() == "#") {
                    asteroids.add(Asteroid(Pair(x.toDouble(), y.toDouble())))
                }
            }
        }
        return asteroids
    }

    private class Asteroid(
        val position: Pair<Double, Double>,
        var angle: Double = 0.0
    ) {
        fun getX() : Double {
            return position.first
        }

        fun getY() : Double {
            return position.second
        }
    }
}