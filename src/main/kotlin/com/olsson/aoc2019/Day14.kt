package com.olsson.aoc2019

import kotlin.math.ceil
import kotlin.math.sign

fun main() {
    val input : List<String> = InputUtils.getLines("Day14.txt").toMutableList()
    val day14 = Day14(input)
    println(day14.calculateCost(amount = 82892754))
    println(day14.findPossibleMaxFuel())
}

class Day14 (
    input : List<String>
) {

    private val reactions = mutableListOf<Reaction>()
    private var inventory = mutableMapOf<String, Long>()

    init {
        for (str in input) {
            reactions.add(Reaction(str))
        }
    }

    fun findPossibleMaxFuel() : Long =
        (0L..trillion).binarySearchBy {
            println("testing $it")
            inventory = mutableMapOf()
            trillion.compareTo(calculateCost(amount = it))
        }

    fun calculateCost(material: String = "FUEL",
                              amount: Long = 1): Long =
        if (material == "ORE") amount
        else {
            val existing = inventory.getOrDefault(material, 0L)
            // If material in inventory, use it
            val needed = if (existing > 0) {
                inventory[material] = (existing - amount).coerceAtLeast(0)
                amount - existing
            } else amount

            if (needed > 0) {
                val reaction = reactions.first{ reaction -> reaction.result.first == material }
                val recipe = reaction.result
                val iterations: Int = ceil(needed.toDouble() / recipe.second).toInt()
                val actuallyProduced = recipe.second * iterations
                // If excess production, store it
                if (needed < actuallyProduced) {
                    inventory[material] = inventory.getOrDefault(material, 0) + actuallyProduced - needed
                }
                // Produce dependencies
                reaction.requirements.map { calculateCost(it.first, (it.second * iterations)) }.sum()
            } else {
                0
            }
        }

    private fun LongRange.binarySearchBy(fn: (Long) -> Int): Long {
        var low = this.first
        var high = this.last
        while (low <= high) {
            val mid = (low + high) / 2
            when (fn(mid).sign) {
                -1 -> high = mid - 1
                1 -> low = mid + 1
                0 -> return mid // Exact match
            }
        }
        return low - 1 // Our next best guess
    }

    private class Reaction (
        input: String
    ) {
        val result: Pair<String, Long>
        val requirements : MutableList<Pair<String, Long>> = mutableListOf()

        init {
            val split = input.split("=>")
            result = Pair(split[1].trim().split(" ")[1], split[1].trim().split(" ")[0].toLong())
            for (str in split[0].split(",")) {
                requirements.add(Pair(str.trim().split(" ")[1], str.trim().split(" ")[0].toLong()))
            }
        }
    }

    companion object {
        private const val trillion = 1_000_000_000_000L
    }
}