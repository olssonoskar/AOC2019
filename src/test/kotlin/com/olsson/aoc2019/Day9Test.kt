package com.olsson.aoc2019

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class Day9Test {

    @Test
    @DisplayName("Day 9 - ex Quine")
    fun quineExample() {

        val quine = """
                    109
                    1
                    204
                    -1
                    1001
                    100
                    1
                    100
                    1008
                    100
                    16
                    101
                    1006
                    101
                    0
                    99
                    """.trimIndent()

        val input = listOf<Long>(109, 1, 204, -1, 1001, 100, 1, 100, 1008, 100, 16, 101, 1006, 101, 0, 99)
        val day9 = Day9(input)
        assertEquals(quine, day9.execute())
    }

    @Test
    @DisplayName("Day 9 - part 1")
    fun part1() {
        val input = InputUtils.getLongs("day9.txt")
        val result = Day9(input, 1).execute()
        assertEquals("3598076521", result)
    }

    @Test
    @DisplayName("Day 9 - part 2")
    fun part2() {
        val input = InputUtils.getLongs("day9.txt")
        val result = Day9(input, 2).execute()
        assertEquals("90722", result)
    }

}