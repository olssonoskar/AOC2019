package com.olsson.aoc2019

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class Day4Test {

    val day4 = Day4()
    val input = Pair(146810, 612564)

    @Test
    @DisplayName("Day 4 - part 1")
    fun part1() {
        assertEquals(1748, day4.part1(input))
    }

    @Test
    @DisplayName("Day 4 - part 2")
    fun part2() {
        assertEquals(1180, day4.part2(input))
    }

}