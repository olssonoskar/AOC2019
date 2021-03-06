package com.olsson.aoc2019

import java.lang.IndexOutOfBoundsException
import java.lang.UnsupportedOperationException
import kotlin.math.pow

fun main() {
    val input = InputUtils.getLongs("day9.txt")
    var day9 = Day9(input)
    day9.execute()
    day9 = Day9(input, mode = 2)
    day9.execute()
}

class Day9 (
    //Must be mutable so we can use more than whats initialized - map would be better
    private val commands: List<Long>,
    private val mode : Long = 1L
){
    private var currentRun = mutableListOf<Long>()
    private var relativeBase = 0
    private var printBuffer = mutableListOf<String>()

    fun execute() : String {
        printBuffer = mutableListOf()
        currentRun = commands.toMutableList()
        var current = 0
        do {
            current = runOperationAt(current)
        } while (currentRun[current] != HALT_OP)
        return printBuffer.reduce{a, b -> "$a\n$b"}
    }

    private fun runOperationAt(index: Int) : Int {
        val param = currentRun[index]
        when (param.getOpCode()) {
            1 -> {
                val add = getFromCommands(index + 1, param.numberAt(2)) +
                    getFromCommands(index + 2, param.numberAt(3))
                setCommands(index + 3, param.numberAt(4), add)
                return index + 4
            }
            2 -> {
                val multi = getFromCommands(index + 1, param.numberAt(2)) *
                    getFromCommands(index + 2, param.numberAt(3))
                setCommands(index + 3, param.numberAt(4), multi)
                return index + 4
            }
            3 -> {
                val input = mode
                setCommands(index + 1, param.numberAt(2), input)
                return index + 2
            }
            4 -> {
                val value = getFromCommands(index + 1, param.numberAt(2))
                println(value)
                printBuffer.add(value.toString())
                return index + 2
            }
            5 -> {  //jump-if
                return if (getFromCommands(index + 1, param.numberAt(2)) != FALSE) {
                    getFromCommands(index + 2, param.numberAt(3)).toInt()
                } else {
                    index + 3
                }
            }
            6 -> {  //jump-if-not
                return if (getFromCommands(index + 1, param.numberAt(2)) == FALSE) {
                    getFromCommands(index + 2, param.numberAt(3)).toInt()
                } else {
                    index + 3
                }
            }
            7 -> {
                val lessThan =
                    if (getFromCommands(index + 1, param.numberAt(2))
                        < getFromCommands(index + 2, param.numberAt(3))) {
                        1L
                    } else {
                        0L
                    }
                setCommands(index + 3, param.numberAt(4), lessThan)
                return index + 4
            }
            8 -> {
                val equals =
                    if (getFromCommands(index + 1, param.numberAt(2))
                        == getFromCommands(index + 2, param.numberAt(3))) {
                        1L
                    } else {
                        0L
                    }
                setCommands(index + 3, param.numberAt(4), equals)
                return index + 4
            }
            9 -> {
                relativeBase += getFromCommands(index + 1, param.numberAt(2)).toInt()
                return index + 2
            }
            else -> throw UnsupportedOperationException("Invalid op code: ${param.getOpCode()}")
        }
    }

    private fun getFromCommands(ref: Int, mode: Int) : Long {
        return when (mode) {
            0 -> getFromReference(ref)
            1 -> currentRun[ref]
            2 -> getFromReference(ref, relativeBase)
            else ->  throw UnsupportedOperationException("Read does not support mode $mode")
        }
    }

    private fun setCommands(ref: Int, mode: Int, value: Long) {
        return when (mode) {
            0 -> setToReference(value, ref)
            2 -> setToReference(value, ref, relativeBase)
            else ->  throw UnsupportedOperationException("Write does not support mode $mode")
        }
    }

    private fun getFromReference(position: Int, relative: Int = 0): Long {
        return try {
            currentRun[currentRun[position].toInt() + relative]
        } catch (e: IndexOutOfBoundsException) {
            0
        }
    }

    private fun setToReference(value: Long, position: Int, relative : Int = 0) {
        if (position >= currentRun.size) {
            paddCommands(position - currentRun.size)
        }
        if (currentRun[position].toInt() + relative >= currentRun.size) {
            paddCommands((currentRun[position].toInt() + relative) - currentRun.size)
        }
        currentRun[currentRun[position].toInt() + relative] = value
    }

    // 'Hacky' solution to fill List to required index
    private fun paddCommands(times: Int) {
        for (i in 0..times) {
            currentRun.add(0)
        }
    }

    private fun Long.numberAt(index: Int): Int {
        return (this / (10.toDouble().pow(index.toDouble()))).toInt() % 10
    }

    private fun Long.getOpCode() : Int {
        return (this % 100L).toInt()
    }

    companion object {
        private const val HALT_OP = 99L
        private const val FALSE = 0L
    }
}