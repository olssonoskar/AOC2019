package com.olsson.aoc2019

fun main() {
    val input = Utils.getAsIntList("day1.txt")
    val day1 = Day1()
    val part1 = day1.fuelRequired(input)
    println("Part1 : Total fuel required: $part1")
    val part2 = day1.fuelRequiredAll(input)
    println("Part2 : Total fuel required including fuel: $part2")
}

class Day1 {

    fun fuelRequired(weight: List<Int>): Int {
        return weight.map {
            calcFuelRequired(it)
        }.sum()
    }

    fun fuelRequiredAll(weight: List<Int>): Int {
        return weight.map {
                calcFuelRequired(it) }
            .map { it + fuelForFuel(it) }
            .sum()
    }

    private fun fuelForFuel(fuelMass: Int) : Int {
        val extra = calcFuelRequired(fuelMass)
        return if (extra <= 0) {
            0
        } else {
            extra + fuelForFuel(extra)
        }
    }

    private fun calcFuelRequired(mass : Int): Int {
        return Math.floorDiv(mass, 3) - 2
    }
}