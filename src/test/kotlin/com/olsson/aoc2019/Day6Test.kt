package com.olsson.aoc2019

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class Day6Test {

    @Test
    @DisplayName("Day 6 - ex 1")
    fun example1() {
        val example = """
            COM)B
            B)C
            C)D
            D)E
            E)F
            B)G
            G)H
            D)I
            E)J
            J)K
            K)L
            """.trimIndent().lines()

        val day6 = Day6(example)
        assertEquals(42, day6.calcOrbits())
    }

    @Test
    @DisplayName("Day 6 - part 1")
    fun part1() {
        val input = InputUtils.getStrings("day6.txt")
        val day6 = Day6(input)
        assertEquals(278744, day6.calcOrbits())
    }

    @Test
    @DisplayName("Day 6 - ex 2")
    fun example2() {
        val example = """
            COM)B
            B)C
            C)D
            D)E
            E)F
            B)G
            G)H
            D)I
            E)J
            J)K
            K)L
            K)YOU
            I)SAN
            """.trimIndent().lines()

        val day6 = Day6(example)
        assertEquals(4, day6.calcOrbitalTransfer())
    }

    @Test
    @DisplayName("Day 6 - part 2")
    fun part2() {
        val input = InputUtils.getStrings("day6.txt")
        val day6 = Day6(input)
        assertEquals(475, day6.calcOrbitalTransfer())
    }

}