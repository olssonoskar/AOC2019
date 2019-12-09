package main

import java.io.File
import java.lang.IndexOutOfBoundsException
import java.lang.UnsupportedOperationException
import kotlin.math.pow

fun main() {
    val input : String = File("src\\resources\\day9.txt").readLines().reduce{ a, b -> "$a,$b" }
    val inputLong = input.split(',').map { it.toLong() }
    val day9 = Day9(inputLong)
    day9.execute()
}

class Day9 (
    //Must be mutable so we can use more than whats initialized
    private val commands: List<Long>
){
    val HALT_OP = 99L
    val FALSE = 0L

    var currentRun = mutableListOf<Long>()
    var relativeBase = 0

    fun execute() {
        currentRun = commands.toMutableList()
        var current = 0
        do {
            current = runOperationAt(current)
        } while (currentRun[current] != HALT_OP)
    }

    fun runOperationAt(index: Int) : Int {
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
                val input = readLine()
                setCommands(index + 1, param.numberAt(2), input!!.toLong())
                return index + 2
            }
            4 -> {
                println(getFromCommands(index + 1, param.numberAt(2)))
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
}