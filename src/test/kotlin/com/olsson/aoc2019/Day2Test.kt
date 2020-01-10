package com.olsson.aoc2019

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class Day2Test {

    @Test
    @DisplayName("Day 2 - ex 1")
    fun example1() {
        val example = listOf(1, 1, 1, 4, 99, 5, 6, 0, 99)
        val result = Day2(example).part1(exampleRun = true)
        assertEquals(30, result)
    }

    @Test
    @DisplayName("Day 2 - part 1")
    fun part1() {
        val input = Utils.getAsIntList("day2.txt")
        val result = Day2(input).part1()
        assertEquals(4945026, result)
    }

    @Test
    @DisplayName("Day 2 - part 2")
    fun part2() {
        val input = Utils.getAsIntList("day2.txt")
        val result = Day2(input).part2()
        assertEquals(Pair(52, 96), result)
    }

}