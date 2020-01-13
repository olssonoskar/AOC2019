package com.olsson.aoc2019

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.math.sign

@ExperimentalCoroutinesApi
fun main() {
    val input : String = InputUtils.getFromResources("day13.txt").readLines().reduce{ a, b -> "$a,$b" }
    val inputLong = input.split(',').map { it.toLong() }
    val day13 = Day13()
    println(day13.part1(inputLong))
    println(day13.part2(inputLong))
}

@ExperimentalCoroutinesApi
class Day13 {

    fun part1(input: List<Long>) = runBlocking {
        val computer = IntcodeComputer(input, output = Channel(Channel.UNLIMITED))
        var blocks = 0
        coroutineScope{
            launch { computer.execute() }
            launch {
                while (!computer.output.isClosedForReceive) {
                    computer.output.receive()
                    computer.output.receive()
                    if(computer.output.receive().toInt() == block) {
                        blocks += 1
                    }
                }
            }
        }
        blocks
    }

    fun part2(input: List<Long>) : Int = runBlocking {
        val program = input.toMutableList()
        program[0] = 2
        val computer = IntcodeComputer(program, output = Channel(Channel.UNLIMITED))

        launch { computer.execute() }

        var paddleX = 0
        var score = 0
        while (!computer.output.isClosedForReceive) {
            val x = computer.output.receive().toInt()
            computer.output.receive()
            val type = computer.output.receive().toInt()
            when {
                x == -1 -> score = type
                type == paddle -> paddleX = x
                type == ball -> {
                    computer.input.send((x - paddleX).sign.toLong())
                }
            }
        }
        score
    }

    companion object {
        private const val block = 2
        private const val paddle = 3
        private const val ball = 4
    }
}