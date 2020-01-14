package com.olsson.aoc2019

import kotlin.math.absoluteValue

class Day16 (
        input : List<Int>
){

    private val initialInput = input
    private val inputSize = input.size

    fun run(iterations : Int) : String {
        var current = initialInput.toIntArray()
        for (i in 0 until iterations) {
            current = fft(current)
        }
        return current.joinToString(separator = "").take(8)
    }

    private fun fft(input : IntArray) : IntArray {
        val result = IntArray(inputSize)
        input.indices.map { index ->
            result[index] = applyPattern(input, buildPattern(index + 1))
        }
        return result
    }

    private fun applyPattern(values : IntArray, pattern : List<Int>) : Int {
        return values.mapIndexed { index, value ->
            value * pattern[index]
        }.sum().absoluteValue % 10
    }

    private fun buildPattern(repeating : Int) : List<Int> {
        val result = mutableListOf<Int>()
        var patternIndex = 0
        while (result.size <= inputSize) {
            repeat(repeating) { result.add(pattern[patternIndex]) }
            patternIndex = (patternIndex + 1) % 4
        }
        result.remove(0)
        return result
    }

    // We want the 8 long intvalue after offset at index x
    // When computing at index x, all values before will be multiplied by 0 and have no impact (due to the pattern)
    // So the value at some index after offset will be the sum of index until end
    // (since all these values will be multiplied by 1, again du to the pattern being repeated)
    // We can thus traverse backwards and sum the previous values for every iteration
    fun part2() : String {
        val offset = initialInput.take(7).joinToString(separator = "").toInt()
        val extendedInput = (offset until 10_000 * inputSize)
                .map { initialInput[it % inputSize] }.toIntArray()
        repeat(100) {
            extendedInput.indices.reversed().fold(0) { sum, index ->
                ((extendedInput[index] + sum) % 10).absoluteValue.also { extendedInput[index] = it }
            }
        }
        return extendedInput.take(8).joinToString(separator = "")
    }

    companion object {
        private val pattern = arrayOf(0, 1, 0, -1)

    }
}
