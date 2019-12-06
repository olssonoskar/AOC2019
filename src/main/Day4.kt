package main

import java.util.function.BiFunction
import kotlin.math.pow

fun main() {
    val input = Pair(146810, 612564)
    val day4 = Day4()
    println("${day4.part1(input)} possible passwords")
    println("${day4.part2(input)} possible passwords")
}
class Day4 {

    fun part1(range: Pair<Int, Int>) : Int {
        return (range.first..range.second)
            .filter {
                neverDecreasing(it)}
            .filter {
                containsDup(it) }
            .count()
    }

    fun part2(range: Pair<Int, Int>) : Int {
        return (range.first..range.second)
            .filter {
                neverDecreasing(it)}
            .filter {
                containsDup(it) }
            .filter {
                containsDoubleDup(it) }
            .count()
    }

    private fun neverDecreasing(number : Int) : Boolean {
        var previousValue = Int.MAX_VALUE
        for (i in 0..5) {
            val nextValue = number.numberAt(i)
            if (nextValue > previousValue) {
                return false
            }
            previousValue = nextValue
        }
        return true
    }

    private fun containsDup(number: Int) : Boolean {
        val seen = mutableSetOf<Int>()
        for (i in 0..5) {
            val nextNumber = number.numberAt(i)
            if (seen.contains(nextNumber)) {
                return true
            }
            seen.add(nextNumber)
        }
        return false
    }

    private fun containsDoubleDup(number: Int) : Boolean {
        val seen = mutableMapOf<Int, Int>()
        for (i in 0..5) {
            val nextNumber = number.numberAt(i)
            seen.merge(nextNumber, 1) { x: Int, y: Int -> x + y}
        }
        return seen.containsValue(2)
    }

    private fun Int.numberAt(index: Int): Int {
        return (this / 10.toDouble().pow(index.toDouble())).toInt() % 10
    }
}

