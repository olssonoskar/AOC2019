package com.olsson.aoc2019

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class Day16Test {

    @Test
    @DisplayName("Day 16 - ex 1.1")
    fun example11() {
        val input = InputUtils.equallySplitList("12345678", isFile = false)
                .map { it.toInt() }
        val day16 = Day16(input)
        assertEquals("01029498", day16.run(4))
    }

    @Test
    @DisplayName("Day 16 - ex 1.2")
    fun example12() {
        val input = InputUtils.equallySplitList("80871224585914546619083218645595", isFile = false)
                .map { it.toInt() }
        val day16 = Day16(input)
        assertEquals("24176176", day16.run(100))
    }

    @Test
    @DisplayName("Day 16 - part 1")
    fun part1() {
        val input = InputUtils.equallySplitList("day16.txt").map { it.toInt() }
        val day16 = Day16(input)
        assertEquals("94935919", day16.run(100))
    }

    @Test
    @DisplayName("Day 16 - ex 2")
    fun example2() {
        val input = InputUtils.equallySplitList("03036732577212944063491565474664", isFile = false)
                .map { it.toInt() }
        val day16 = Day16(input)
        assertEquals("84462026", day16.part2())
    }

    @Test
    @DisplayName("Day 16 - part 2")
    fun part2() {
        val input = InputUtils.equallySplitList("day16.txt").map { it.toInt() }
        val day16 = Day16(input)
        assertEquals("24158285", day16.part2())
    }
}