package com.olsson.aoc2019

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class Day3Test {

    private val day3 = Day3()

    @Test
    @DisplayName("Day 3 - ex 1")
    fun example1() {
        val example = arrayListOf("R75,D30,R83,U83,L12,D49,R71,U7,L72",
                "U62,R66,U55,R34,D71,R55,D58,R83")
        val result = day3.part1(example)
        assertEquals(159, result)
    }

    @Test
    @DisplayName("Day 3 - part 1")
    fun part1() {
        val input = Utils.getLines("day3.txt")
        val result = day3.part1(input)
        assertEquals(3247, result)
    }

    @Test
    @DisplayName("Day 3 - ex 2")
    fun example2() {
        val example = arrayListOf("R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51",
                "U98,R91,D20,R16,D67,R40,U7,R15,U6,R7")
        val result = day3.part2(example)
        assertEquals(410, result)
    }

    @Test
    @DisplayName("Day 3 - part 2")
    fun part2() {
        val input = Utils.getLines("day3.txt")
        val result = day3.part2(input)
        assertEquals(48054, result)
    }

}