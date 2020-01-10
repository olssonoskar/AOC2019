package com.olsson.aoc2019

import java.lang.IllegalArgumentException
import java.lang.UnsupportedOperationException
import kotlin.math.abs

fun main() {
    val input : List<String> = Utils.getLines("day3.txt")
    val day3 = Day3()
    val intersection = day3.part1(input)
    println("Closest intersection is $intersection")
    day3.part2(input)
}

class Day3 {

    fun part1(wires: List<String>) : Int {
        val firstWirePoints = findPoints(wires[0].split(","))
        val secondWirePoints = findPoints(wires[1].split(","))
        val closest = findClosestIntersection(firstWirePoints, secondWirePoints)
        return if ( closest != null) {
            abs(closest.position.first) + abs(closest.position.second)
        } else {
            println("No intersection found")
            -1
        }
    }

    fun part2(wires: List<String>) : Int {
        val firstWirePoints = findPoints(wires[0].split(","))
        val secondWirePoints = findPoints(wires[1].split(","))
        val closest = findClosestIntersectionStepwise(firstWirePoints, secondWirePoints)
        println("Closest point is steps is ${closest.position} with ${closest.steps} steps")
        return closest.steps
    }

    private fun findPoints(wire: List<String>): Set<Point> {
        val points = mutableSetOf<Point>()
        var currentPosition = Point(0, Pair(0, 0))
        for (path in wire) {
            val newPoint = followWire(path, currentPosition)
            points.addAll(currentPosition.allPointsBetween(newPoint))
            currentPosition = newPoint
        }
        return points.filter { point -> point.position != Pair(0, 0) }.toCollection(mutableSetOf())
    }

    private fun followWire(path: String, start: Point) : Point {
        val direction = path.substring(0,1)
        val length = path.substring(1).toInt()
        return when(direction) {
            "R" -> Point(start.steps + length, Pair(start.getX() + length, start.getY()))
            "L" -> Point(start.steps + length, Pair(start.getX() - length, start.getY()))
            "U" -> Point(start.steps + length, Pair(start.getX(), start.getY() + length))
            "D" -> Point(start.steps + length, Pair(start.getX(), start.getY() - length))
            else -> throw UnsupportedOperationException("Unknown command: $direction")
        }
    }

    private fun findClosestIntersection(wire1: Set<Point>, wire2: Set<Point>) : Point? {
        val wire2Positions = wire2.map { point -> point.position  }.toCollection(mutableSetOf())
        return wire1
            .filter { point -> wire2Positions.contains(point.position) }
            .minBy { point -> abs(point.getX()) + abs(point.getY()) }
    }

    private fun findClosestIntersectionStepwise(points1: Set<Point>, points2: Set<Point>) : Point {
        val intersectionPoints = sumStepOnIntersectingPoints(points1, points2)
        return intersectionPoints.minBy { point -> point.steps }
                ?: throw IllegalArgumentException("No minimum step point for this setup")
    }

    private fun sumStepOnIntersectingPoints(wire1: Set<Point>, wire2: Set<Point>) : List<Point> {
        val wire2Positions = wire2.map { point -> point.position  }.toCollection(mutableSetOf())
        return wire1
            .filter { point -> wire2Positions.contains(point.position) }
            .map { point ->
                val other = wire2.first { otherPoint -> point.position == otherPoint.position }
                Point(point.steps + other.steps, point.position)
            }
            .toCollection(mutableListOf())
    }

    private class Point(
        val steps: Int,
        val position: Pair<Int, Int>
    ) {
        fun getX() : Int = position.first
        fun getY() : Int = position.second

        fun allPointsBetween(point : Point) : Set<Point> {
            val newPath = mutableSetOf<Point>()
            var currentStep = 0
            if(getX() == point.getX()) {
                if (getY() < point.getY()) {
                    for (i in getY()..point.getY()) {
                        newPath.add(Point(steps + currentStep, Pair(getX(), i)))
                        currentStep ++
                    }
                } else {
                    for (i in getY() downTo point.getY()) {
                        newPath.add(Point(steps + currentStep, Pair(getX(), i)))
                        currentStep ++
                    }
                }
            } else if (getY() == point.getY()) {
                if (getX() < point.getX()) {
                    for (i in getX()..point.getX()) {
                        newPath.add(Point(steps + currentStep, Pair(i, getY())))
                        currentStep ++
                    }
                }
                else {
                    for (i in getX() downTo point.getX()) {
                        newPath.add(Point(steps + currentStep, Pair(i, getY())))
                        currentStep ++
                    }
                }
            } else {
                throw UnsupportedOperationException("Points are neither on same x nor y value")
            }
            return newPath
        }
    }
}