package main

import kotlinx.coroutines.*
import java.io.File

@ExperimentalCoroutinesApi
fun main() {
    val input : String = File("src\\resources\\day11.txt").readLines().reduce{ a, b -> "$a,$b" }
    val inputLong = input.split(',').map { it.toLong() }
    println(Day11(inputLong).paintShip().size)
    printMap(Day11(inputLong).paintShip(1))
}

fun printMap(map: MutableMap<Pair<Int, Int>, Int>) {
    val endX = map.maxBy { it.key.first }?.key?.first
    val startX = map.minBy { it.key.first }?.key?.first
    val endY = map.maxBy { it.key.second }?.key?.second
    val startY = map.minBy { it.key.second }?.key?.second
    if(endX == null || endY == null || startX == null || startY == null) {
        throw IllegalArgumentException("Not a valid map")
    }
    for (x in startX..endX) {
        for (y in startY..endY) {
            if (map[Pair(x, y)] == 1) {
                print("#")
            } else {
                print(" ")
            }
        }
        println()
    }
}

class Day11 (
    val input : List<Long>
){

    fun paintShip(startColor: Int = 0) = runBlocking {
        val hull = mutableMapOf<Pair<Int, Int>, Int>()
        hull[Pair(0,0)] = startColor
        val robot = Robot(input)
        coroutineScope{
            launch { robot.comp.execute() }
            launch {
                robot.comp.input.send(getColorAtPanel(robot.pos.first, robot.pos.second, hull).toLong())
                while (!robot.comp.output.isClosedForReceive) {
                    val color = robot.comp.output.receive().toInt()
                    hull[robot.pos] = color
                    val direction = robot.comp.output.receive().toInt()
                    robot.move(direction)
                    robot.comp.input.send(getColorAtPanel(robot.pos.first, robot.pos.second, hull).toLong())
                }
            }
            hull
        }
    }

    private fun getColorAtPanel(x: Int, y: Int, hull: Map<Pair<Int,Int>, Int>) : Int {
        return hull.getOrDefault(Pair(x, y), 0)
    }

    private class Robot (
        input: List<Long>,
        var pos : Pair<Int, Int> = Pair(0, 0),
        var dir : Int = 0

    ) {
        val comp = IntcodeComputer(input)

        fun move(direction: Int) {
            if (direction == 1) {
                dir = (dir + 1) % 4
            } else {
                dir = (dir - 1)
                if (dir < 0) {
                    dir = 3
                }
            }

            when (dir) {
                0 -> pos = Pair(pos.first, pos.second + 1)
                1 -> pos = Pair(pos.first + 1, pos.second)
                2 -> pos = Pair(pos.first, pos.second - 1)
                3 -> pos = Pair(pos.first - 1, pos.second)
            }

        }
    }

}