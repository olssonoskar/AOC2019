package com.olsson.aoc2019

import kotlin.math.abs

fun main() {
    val input = InputUtils.getFromResources("day12.txt")
        .readLines().reduce{ a, b -> "$a,$b" }
        .replace(Regex("[<>xyz=]+"), "")
        .split(",")
        .map { it.trim() }
        .map { it.toInt() }
    val day12 = Day12(input)
    day12.runSimulation(1000)

}

class Day12 (
    input: List<Int>
){
    private val moons = mutableListOf<Moon>()
    init {
        moons.add(Moon("Io", input[0], input[1], input[2]))
        moons.add(Moon("Europa", input[3], input[4], input[5]))
        moons.add(Moon("Ganymede", input[6], input[7], input[8]))
        moons.add(Moon("Callisto", input[9], input[10], input[11]))
    }

    fun runSimulation(steps: Int) {
        for (i in 0 until steps) {
            calcGravitationalPull()
            moons.forEach{ moon ->
                moon.applyGravity()
                moon.applyVelocity()
            }
        }

        moons.forEach { moon ->
            println("${moon.getPosition()} - ${moon.getVelocity()} ")
        }

        val energy = moons
            .map { moon -> moon.energy() }
            .sum()

        println(energy)
    }

    private fun calcGravitationalPull() {
        moons.map {
            moon -> moons.map {
                // Calc for all since its own will have 0 effect
                otherMoon -> moon.calcGravity(otherMoon)
            }
        }
    }

    private class Moon (
        val name: String,
        startX: Int = 0,
        startY: Int = 0,
        startZ: Int = 0
    ) {
        private var x = 0
        private var y = 0
        private var z = 0
        private var gravitationalPull = Triple(0, 0, 0)
        private var velocity = Triple(0, 0, 0)

        init {
            x = startX
            y = startY
            z = startZ
        }

        fun getPosition() : Triple<Int, Int, Int> {
            return Triple(x, y, z)
        }

        fun getVelocity() : Triple<Int, Int, Int> {
            return velocity
        }

        fun applyVelocity() {
            x += velocity.first
            y += velocity.second
            z += velocity.third
        }

        fun calcGravity(other: Moon) {
            val deltaX = gravityEffect(x, other.x)
            val deltaY = gravityEffect(y, other.y)
            val deltaZ = gravityEffect(z, other.z)
            gravitationalPull = Triple(
                gravitationalPull.first + deltaX,
                gravitationalPull.second + deltaY,
                gravitationalPull.third + deltaZ
            )
        }

        fun gravityEffect(origin: Int, other: Int) : Int {
            return when {
                origin < other -> 1
                origin > other -> -1
                else -> 0
            }
        }

        fun applyGravity() {
            velocity = Triple(
                velocity.first + gravitationalPull.first,
                velocity.second + gravitationalPull.second,
                velocity.third + gravitationalPull.third
            )
            gravitationalPull = Triple(0, 0, 0)
        }

        fun energy() : Int {
            val potential = abs(x) + abs(y) + abs(z)
            val kinetic = abs(velocity.first) + abs(velocity.second) + abs(velocity.third)
            val energy = potential * kinetic
            println("$name has $potential * $kinetic = $energy")
            return energy
        }
    }
}