package com.olsson.aoc2019

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class Day1Test {

    private val day1 = Day1()
    private val example = listOf(24, 12, 20)
    private val example2 = listOf(14, 1969, 100756)

    @Test
    @DisplayName("Day 1 - Ex 1")
    fun example1() {
        val result = day1.fuelRequired(example)
        assertEquals( 6 + 2 + 4, result)
    }

    @Test
    @DisplayName("Day 1 - Part 1")
    fun actual1() {
        val result = day1.fuelRequired(Utils.getAsIntList("day1.txt"))
        assertEquals( 3380880, result)
    }

    @Test
    @DisplayName("Day 1 - Ex 2")
    fun example2() {
        val result = day1.fuelRequiredAll(example2)
        assertEquals( 2 + 966 + 50346, result)
    }

    @Test
    @DisplayName("Day 1 - Part 2")
    fun actual2() {
        val result = day1.fuelRequiredAll(Utils.getAsIntList("day1.txt"))
        assertEquals( 5068454, result)
    }

}