package com.olsson.aoc2019

import java.io.File
import java.lang.UnsupportedOperationException
import kotlin.math.pow

fun main() {
    val input : String = Utils.getFromResources("day5.txt").readLines().reduce{ a, b -> "$a,$b" }
    val inputInt = input.split(',').map { it.toInt() }
    val day5 = Day5(inputInt)
    day5.execute()
}

class Day5 (
    private val commands: List<Int>
){
    var currentRun = mutableListOf<Int>()

    fun execute() {
        currentRun = commands.toMutableList()
        var current = 0
        do {
            current = runOperationAt(current)
        } while (currentRun[current] != 99)
    }

    fun runOperationAt(index: Int) : Int {
        val param = currentRun[index]
        when (param.getOpCode()) {
            1 -> {
                currentRun[currentRun[index + 3]] = getInput(index + 1, param.numberAt(2)) +
                    getInput(index + 2, param.numberAt(3))
                return index + 4
            }
            2 -> {
                currentRun[currentRun[index + 3]] = getInput(index + 1, param.numberAt(2)) *
                    getInput(index + 2, param.numberAt(3))
                return index + 4
            }
            3 -> {
                println("Reading input from user, currently mocked to take 1")
                currentRun[currentRun[index + 1]] = 5 //Should take user input, mock behavior atm
                return index + 2
            }
            4 -> {
                println(getInput(index+1, param.numberAt(2)))
                return index + 2
            }
            5 -> {
                return if (getInput(index + 1, param.numberAt(2)) != 0) {
                    getInput(index + 2, param.numberAt(3))
                } else {
                    index + 3
                }
            }
            6 -> {
                return if (getInput(index + 1, param.numberAt(2)) == 0) {
                    getInput(index + 2, param.numberAt(3))
                } else {
                    index + 3
                }
            }
            7 -> {
                currentRun[currentRun[index + 3]] =
                    if (getInput(index + 1, param.numberAt(2))
                        < getInput(index + 2, param.numberAt(3))) {
                        1
                    } else {
                        0
                    }
                return index + 4
            }
            8 -> {
                currentRun[currentRun[index + 3]] =
                    if (getInput(index + 1, param.numberAt(2))
                        == getInput(index + 2, param.numberAt(3))) {
                        1
                    } else {
                        0
                    }
                return index + 4
            }
            else -> throw UnsupportedOperationException("Invalid op code: ${param.getOpCode()}")
        }
    }

    private fun getInput(ref: Int, mode: Int) : Int {
        return when (mode) {
            0 -> getFromReference(ref)
            1 -> currentRun[ref]
            else ->  throw UnsupportedOperationException("Does not support mode $mode")
        }
    }

    private fun getFromReference(position: Int): Int {
        return currentRun[currentRun[position]]
    }

    private fun Int.numberAt(index: Int): Int {
        return (this / 10.toDouble().pow(index.toDouble())).toInt() % 10
    }

    private fun Int.getOpCode() : Int {
        return this % 100
    }
}