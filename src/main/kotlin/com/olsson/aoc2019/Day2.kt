package com.olsson.aoc2019

import java.io.File

fun main() {
    val input : String = Utils.getFromResources("day2.txt").readLines().reduce{ a, b -> "$a,$b" }
    val inputs = input.split(',').map { it.toInt() }
    val day2 = Day2(inputs, 19_690_720)
    val result = day2.part1()
    println("The result of part1 is $result")
    val result2 = day2.part2()
    println("The result of part 2 is $result2")

}

class Day2 (
    private val commands: List<Int>,
    private val searched: Int
){
    var currentRun = mutableListOf<Int>()

    fun part2(): Pair<Int, Int> {
        var verb = 0
        var noun = 0
        var passed = false
        do {
            currentRun = commands.toMutableList()
            setVerbAndNoun(verb, noun)
            execute()

            if (currentRun[0] == searched) {
                return Pair(verb, noun)
            } else if (currentRun[0] < searched && !passed) {
                verb++
            } else if (!passed) {
                passed = true
                verb--
                noun++
            } else {
                noun++
            }
        } while (currentRun[0] != searched)
        return Pair(0, 0)
    }

    fun part1(): Int {
        currentRun = commands.toMutableList()
        restoreAlarm()
        execute()
        return currentRun[0]
    }

    fun execute() {
        var current = 0
        do {
            runOperationAt(current)
            current += 4
        } while (currentRun[current] != 99)
    }

    fun runOperationAt(index: Int) {
        when (currentRun[index]) {
            1 -> currentRun[currentRun[index+3]] = getFromReference(index, 1) + getFromReference(index, 2)
            2 -> currentRun[currentRun[index+3]] = getFromReference(index, 1) * getFromReference(index, 2)
        }
    }

    private fun restoreAlarm() {
        setVerbAndNoun(12, 2)
    }

    private fun setVerbAndNoun(noun: Int, verb: Int) {
        currentRun[1] = noun
        currentRun[2] = verb
    }

    private fun getFromReference(index: Int, argNum: Int): Int {
        return currentRun[currentRun[index + argNum]]
    }
}