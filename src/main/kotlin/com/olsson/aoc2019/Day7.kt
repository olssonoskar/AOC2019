package com.olsson.aoc2019

import java.io.File
import java.lang.UnsupportedOperationException
import kotlin.math.pow

fun main() {
    val input : String = Utils.getFromResources("day7.txt").readLines().reduce{ a, b -> "$a,$b" }
    val inputInt = input.split(',').map { it.toInt() }
    val day7 = Day7(inputInt)
    println("Max should be ${day7.searchMax()}")
    println("Max should be ${day7.searchMaxWithFeedback()}")
}

class Day7 (
    private val commands: List<Int>
){
    private val HALT_OP = 99
    private val WRITE_OP = 4

    private var currentCommands = mutableListOf<Int>()
    // Used in part 2
    private var amps = listOf<Amplifier>()
    
    private var phaseSetting = 0
    private var inputSignal = 0
    private var output = 0

    fun searchMax() : Int {
        var max = 0
        for (i in 1234..43210) {
            if (validSetting(i, 0, 4)) {
                var runOutput = 0
                for (j in 4 downTo 0) {
                    runOutput = execute(i.numberAt(j), runOutput)
                }
                if (runOutput > max) {
                    max = runOutput
                }
            }
        }
        return max
    }

    fun searchMaxWithFeedback() : Int {
        var max = 0
        for (setting in 56789..98765) {
            if (validSetting(setting, 5 , 9 )) {
                amps = setupAmps()
                var runOutput = 0
                var amp = 4
                while (amps[amp].pc != HALT_OP) {
                    runOutput = executeWithFeedback(setting.numberAt(amp), runOutput, amp)
                    amp = nextAmp(amp)
                }
                if (runOutput > max) {
                    max = runOutput
                }
            }
        }
        return max
    }

    private fun validSetting(number: Int, min: Int, max: Int) : Boolean {
        val seen = mutableSetOf<Int>()
        for (index in 4 downTo 0) {
            val toCheck = number.numberAt(index)
            if (seen.contains(toCheck) || toCheck !in min..max) {
                return false
            }
            seen.add(toCheck)
        }
        return true
    }

    private fun execute(setting: Int, signal: Int): Int {
        clearState()
        currentCommands = commands.toMutableList()
        phaseSetting = setting
        inputSignal = signal
        var programCounter = 0

        do {
            programCounter = runOperationAt(programCounter)
        } while (currentCommands[programCounter].getOpCode() != HALT_OP)
        return output
    }

    private fun executeWithFeedback(setting: Int, signal: Int, amp: Int): Int {
        currentCommands = amps[amp].commands
        var programCounter = amps[amp].pc
        phaseSetting = setting
        inputSignal = signal

        if (currentOperation(programCounter) == HALT_OP) {
            amps[amp].pc = HALT_OP
            return signal
        }

        do {
            programCounter = runOperationAt(programCounter)
        } while (currentOperation(programCounter) != WRITE_OP &&
            currentOperation(programCounter) != HALT_OP)

        if (currentOperation(programCounter) == WRITE_OP) {
            programCounter = runOperationAt(programCounter)
        }
        amps[amp].pc = programCounter
        return output
    }

    private fun currentOperation(pc: Int) : Int {
        return currentCommands[pc].getOpCode()
    }

    private fun runOperationAt(index: Int) : Int {
        val param = currentCommands[index]
        when (param.getOpCode()) {
            1 -> {
                currentCommands[currentCommands[index + 3]] = getInput(index + 1, param.numberAt(2)) +
                    getInput(index + 2, param.numberAt(3))
                return index + 4
            }
            2 -> {
                currentCommands[currentCommands[index + 3]] = getInput(index + 1, param.numberAt(2)) *
                    getInput(index + 2, param.numberAt(3))
                return index + 4
            }
            3 -> {
                //Weak assumption, always init amp by reading setting (e.g. when index 0)
                val input = when (index) {
                    0 -> phaseSetting
                    else-> inputSignal
                }
                currentCommands[currentCommands[index + 1]] = Integer.parseInt(input.toString())
                return index + 2
            }
            4 -> {
                val out = getInput(index+1, param.numberAt(2))
                output = out
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
                currentCommands[currentCommands[index + 3]] =
                    if (getInput(index + 1, param.numberAt(2))
                        < getInput(index + 2, param.numberAt(3))) {
                        1
                    } else {
                        0
                    }
                return index + 4
            }
            8 -> {
                currentCommands[currentCommands[index + 3]] =
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
            1 -> currentCommands[ref]
            else ->  throw UnsupportedOperationException("Does not support mode $mode")
        }
    }

    private fun getFromReference(position: Int): Int {
        return currentCommands[currentCommands[position]]
    }

    private fun Int.numberAt(index: Int): Int {
        return (this / 10.toDouble().pow(index.toDouble())).toInt() % 10
    }

    private fun Int.getOpCode() : Int {
        return this % 100
    }

    private fun clearState() {
        phaseSetting = 0
        inputSignal = 0
        output = 0
    }

    private fun nextAmp(current: Int) : Int {
        val next = current - 1
        if (next < 0) {
            return 4
        }
        return next
    }

    private fun setupAmps(): List<Amplifier> {
        return listOf(
                Amplifier(commands.toMutableList(), 0),
                Amplifier(commands.toMutableList(), 0),
                Amplifier(commands.toMutableList(), 0),
                Amplifier(commands.toMutableList(), 0),
                Amplifier(commands.toMutableList(), 0)
        )
    }

    private class Amplifier(
        val commands: MutableList<Int>,
        var pc: Int
    )
}