package com.olsson.aoc2019

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class Day7Test {

    @Test
    @DisplayName("Day 7 - ex 1.1")
    fun example11() {
        val day7 = Day7(listOf(3, 15, 3, 16, 1002, 16, 10, 16, 1, 16, 15, 15, 4, 15, 99, 0, 0))
        val result = day7.searchMax()
        assertEquals(43210, result)
    }

    @Test
    @DisplayName("Day 7 - ex 1.2")
    fun example12() {
        val day7 = Day7(listOf(3, 31, 3, 32, 1002, 32, 10, 32, 1001, 31, -2, 31, 1007, 31, 0, 33,
                1002, 33, 7, 33, 1, 33, 31, 31, 1, 32, 31, 31, 4, 31, 99, 0, 0, 0))
        val result = day7.searchMax()
        assertEquals(65210, result)
    }

    @Test
    @DisplayName("Day 7 - part 1")
    fun part1() {
        val input = InputUtils.getInts("day7.txt")
        val day7 = Day7(input)
        val result = day7.searchMax()
        assertEquals(116680, result)
    }

    @Test
    @DisplayName("Day 7 - ex 2")
    fun example2() {
        val day7 = Day7(listOf(3, 52, 1001, 52, -5, 52, 3, 53, 1, 52, 56, 54, 1007, 54, 5, 55, 1005, 55, 26, 1001, 54,
                -5, 54, 1105, 1, 12, 1, 53, 54, 53, 1008, 54, 0, 55, 1001, 55, 1, 55, 2, 53, 55, 53, 4,
                53, 1001, 56, -1, 56, 1005, 56, 6, 99, 0, 0, 0, 0, 10))
        val result = day7.searchMaxWithFeedback()
        assertEquals(18216, result)
    }

    @Test
    @DisplayName("Day 7 - part 2")
    fun part2() {
        val input = InputUtils.getInts("day7.txt")
        val day7 = Day7(input)
        val result = day7.searchMaxWithFeedback()
        assertEquals(89603079, result)
    }
}