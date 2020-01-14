package com.olsson.aoc2019

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.lang.IllegalArgumentException

fun main() {
    val input = InputUtils.getLongs("day17.txt")
    //var day17 = Day17(input)
    //println(day17.part1())
    val overrideCommand = input.toMutableList()
    overrideCommand[0] = 2L
    val day17 = Day17(overrideCommand)
    println(day17.part2())
}

class Day17 (
        input : List<Long>
){
    private val computer = IntcodeComputer(input, output = Channel(Channel.UNLIMITED))
    private val map = mutableMapOf<Pair<Int, Int>, Char>()

    // Since we cant use arbitrary movement i also went and handcrafted this solution...
    private val commands = listOf(
            "A,C,A,C,A,B,A,B,C,B",
            "R,6,L,10,R,10,R,10",
            "R,6,L,6,6,L,10",
            "L,10,L,6,6,R,10",
            "N")

    fun part2() : Int = runBlocking {
        val work = launch {
            computer.execute()
        }

        println("Sending input")
        commands.forEach{ line ->
            line.forEach { char ->
                computer.input.send(char.toLong())
            }
            computer.input.send(10)
        }
        work.join()
        var out = 0L
        computer.output.consumeEach {
            out = it
        }
        out.toInt()
    }

    fun part1() : Int {
        getPicture()
        return findIntersections().map {
            it.first * it.second
        }.sum()
    }
    private fun getPicture() = runBlocking{
        launch { computer.execute() }

        var column = 0
        var row = 0
        computer.output.consumeEach {
            if (it == 10L) {
                row += 1
                column = 0
                println()
            } else {
                print("${it.toChar()} ")
                map[Pair(column, row)] = (it.toChar())
                column += 1
            }
        }
    }

    private fun findIntersections() : List<Pair<Int, Int>> {
        return map.filter { entry -> entry.value == '#' }
                .map { entry -> entry.key  }
                .filter { key -> isIntersection(key) }
    }

    private fun isIntersection(pos : Pair<Int, Int>) : Boolean {
        return listOf(above(pos), below(pos), left(pos), right(pos))
                .all { map.getOrDefault(it, '.') == '#' }
    }

    private fun above(pos : Pair<Int, Int>) = pos.copy(second = pos.second + 1)
    private fun below(pos : Pair<Int, Int>) = pos.copy(second = pos.second - 1)
    private fun right(pos : Pair<Int, Int>) = pos.copy(first = pos.first + 1)
    private fun left(pos : Pair<Int, Int>) = pos.copy(first = pos.first - 1)

}