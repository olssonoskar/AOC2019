package com.olsson.aoc2019

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import java.lang.IllegalArgumentException
import java.util.*

@ExperimentalCoroutinesApi
fun main() {
    val input = InputUtils.getLongs("day15.txt")
    val day15 = Day15(input)
    day15.printMap()
    println(day15.part1())
    println(day15.part2())
}

@ExperimentalCoroutinesApi
class Day15 (
        program : List<Long>
){

    private val computer = IntcodeComputer(program)
    private val area : MutableMap<Pair<Int, Int>, Long> = searchMaze()
    private val oxygen = area.entries.first{ it.value == oxygenSystem }.key

    private fun searchMaze() = runBlocking {
        val robot = async {
            computer.execute()
        }
        searchNode(Pair(0, 0), mutableMapOf(Pair(0, 0) to space)).also {
            robot.cancel()
        }
    }

    private suspend fun searchNode(origin : Pair<Int, Int>, area : MutableMap<Pair<Int, Int>, Long>) : MutableMap<Pair<Int, Int>, Long> {
        origin.neighbors()
                .filter { neighbor -> neighbor !in area }
                .forEach { neighbor ->
                    computer.input.send(origin.directionToNode(neighbor))
                    when(val result = computer.output.receive()) {
                        space, oxygenSystem -> {
                            area[neighbor] = result
                            searchNode(neighbor, area)
                            computer.input.send(neighbor.directionToNode(origin))
                            computer.output.receive()
                        }
                        wall -> area[neighbor] = wall // robot not moved and nothing to search
                        else -> throw IllegalArgumentException("Invalid arg received")
                    }
                }
        return area
    }

    fun part1() : Int  {
        return findPath (to = oxygen) { area.getOrDefault(it, wall) != wall }.first().size - 1
    }

    // We want longest path which will be last item in sequence
    fun part2() : Int  {
        return findPath (from = oxygen) { area.getOrDefault(it, wall) != wall }.last().size - 1
    }

    private fun findPath(
            from : Pair<Int, Int> = Pair(0, 0),
            to : Pair<Int, Int>? = null,
            validNeighbor: (Pair<Int, Int>) -> Boolean) : Sequence<List<Pair<Int, Int>>> = sequence {

        val visitedNodes = mutableSetOf<Pair<Int, Int>>()
        val queue = ArrayDeque<MutableList<Pair<Int, Int>>>().apply { add(mutableListOf(from)) }
        while (queue.isNotEmpty()) {
            val currentPath = queue.pop()
            // Found oxygenSystem
            if (currentPath.last() == to) {
                yield(currentPath)
            }
            if (currentPath.last() !in visitedNodes) {
                visitedNodes.add(currentPath.last())
                val neighbors = currentPath.last().neighbors()
                        .filter(validNeighbor)
                        .filter{it !in visitedNodes}
                if (neighbors.isEmpty() && to == null) {
                    yield(currentPath) // Dead end
                } else {
                    // For current path, add all neighbors as new possible paths
                    neighbors.forEach {neighbor ->
                        val newPath = mutableListOf<Pair<Int, Int>>()
                        newPath.addAll(currentPath)
                        newPath.add(neighbor)
                        queue.add(newPath)
                    }
                }
            }
        }
    }

    private fun Pair<Int,Int>.directionToNode(node : Pair<Int, Int>) : Long {
        return when(node) {
            northOf(this) -> 1
            southOf(this) -> 2
            westOf(this) -> 3
            eastOf(this) -> 4
            else -> throw IllegalArgumentException("No relation between $this and $node")
        }
    }

    private fun Pair<Int, Int>.neighbors() : List<Pair<Int,Int>> {
        return listOf(northOf(this), eastOf(this), southOf(this), westOf(this))
    }

    private fun northOf(node : Pair<Int, Int>) : Pair<Int, Int> = Pair(node.first, node.second + 1)
    private fun eastOf(node : Pair<Int, Int>) : Pair<Int, Int> = Pair(node.first + 1, node.second)
    private fun southOf(node : Pair<Int, Int>) : Pair<Int, Int> = Pair(node.first, node.second - 1)
    private fun westOf(node : Pair<Int, Int>) : Pair<Int, Int> = Pair(node.first - 1, node.second)

    fun printMap() {
        for (y in -20..22) {
            var str = ""
            for (x in -22..20) {
                if (x == 0 && y == 0) {
                    str += " X "
                    continue
                }
                str += when(area[Pair(x, y)]) {
                    2L -> " O "
                    1L -> " . "
                    0L -> " # "
                    else -> " ? "
                }
            }
            println(str)
        }
    }

    companion object {
        private const val wall = 0L
        private const val space = 1L
        private const val oxygenSystem = 2L


    }
}