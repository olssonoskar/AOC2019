package com.olsson.aoc2019

import kotlinx.coroutines.channels.Channel
import java.lang.IndexOutOfBoundsException
import java.lang.UnsupportedOperationException
import kotlin.math.pow

class IntcodeComputer (
    commands: List<Long>,
    val input: Channel<Long> = Channel(Channel.UNLIMITED),
    val output: Channel<Long> = Channel(Channel.CONFLATED)
){
    private val HALT_OP = 99L
    private val FALSE = 0L

    private var relativeBase = 0
    private var programCounter = 0
    var loadedMemory = commands.toMutableList()

    suspend fun execute() {
        do {
            programCounter = runOperationAt(programCounter)
        } while (loadedMemory[programCounter] != HALT_OP)
        println("Halting robot - closing output")
        output.close()
    }

    fun isHalted() : Boolean {
        return loadedMemory[programCounter] == HALT_OP
    }

    private suspend fun runOperationAt(index: Int) : Int {
        val param = loadedMemory[index]
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
                setCommands(index + 1, param.numberAt(2), input.receive())
                return index + 2
            }
            4 -> {
                output.send(getFromCommands(index + 1, param.numberAt(2)))
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
            1 -> loadedMemory[ref]
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
            loadedMemory[loadedMemory[position].toInt() + relative]
        } catch (e: IndexOutOfBoundsException) {
            0
        }
    }

    private fun setToReference(value: Long, position: Int, relative : Int = 0) {
        if (position >= loadedMemory.size) {
            paddCommands(position - loadedMemory.size)
        }
        if (loadedMemory[position].toInt() + relative >= loadedMemory.size) {
            paddCommands((loadedMemory[position].toInt() + relative) - loadedMemory.size)
        }
        loadedMemory[loadedMemory[position].toInt() + relative] = value
    }

    // 'Hacky' solution to fill List to required index
    private fun paddCommands(times: Int) {
        for (i in 0..times) {
            loadedMemory.add(0)
        }
    }

    private fun Long.numberAt(index: Int): Int {
        return (this / (10.toDouble().pow(index.toDouble()))).toInt() % 10
    }

    private fun Long.getOpCode() : Int {
        return (this % 100L).toInt()
    }
}