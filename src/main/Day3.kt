package main

import java.io.File
import java.lang.UnsupportedOperationException
import kotlin.math.abs

fun main() {
    val input : List<String> = File("src\\resources\\day3.txt").readLines().toCollection(arrayListOf())
    val day3 = Day3()
    val intersection = day3.part1(input)
    println("Closest intersection is $intersection = ${abs(intersection.first) + abs(intersection.second)}")
    println(day3.part2(input))
}

class Day3 {

    fun part1(wires: List<String>) : Pair<Int, Int> {
        val firstWirePoints = findPoints(wires[0].split(","))
        val secondWirePoints = findPoints(wires[1].split(","))
        val closest = findClosestIntersection(firstWirePoints, secondWirePoints)
        return if ( closest != null) {
            closest.position
        } else {
            println("No intersection found")
            Pair(0, 0)
        }
    }

    fun part2(wires: List<String>) : String {
        val firstWirePoints = findPoints(wires[0].split(","))
        val secondWirePoints = findPoints(wires[1].split(","))
        val closest = findClosestIntersectionStepwise(firstWirePoints, secondWirePoints)
        return "Closest point is steps is ${closest.position} with ${closest.steps} steps"
    }

    private fun findPoints(wire: List<String>): Set<Point> {
        val points = mutableSetOf<Point>()
        var currentPosition = Point(0,Pair(0,0))
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

    private fun findClosestIntersection(points1: Set<Point>, points2: Set<Point>) : Point? {
        val points2Positions = points2.map { point -> point.position  }.toCollection(mutableSetOf())
        return points1
            .filter { point -> points2Positions.contains(point.position) }
            .minBy { point -> abs(point.getX()) + abs(point.getY()) }
    }

    private fun findClosestIntersectionStepwise(points1: Set<Point>, points2: Set<Point>) : Point {
        val intersectionPoints = getIntersectingPoints(points1, points2)
        var candidate = Point(Int.MAX_VALUE, Pair(0, 0))
        for(point in intersectionPoints.first) {
            for(point2 in intersectionPoints.second) {
                if(point.position == point2.position) {
                    val combinedSteps = point.steps + point2.steps
                    if (combinedSteps < candidate.steps) {
                        candidate = Point(combinedSteps, Pair(point.getX(), point.getY()))
                    }
                }
            }
        }
        return candidate
    }

    private fun getIntersectingPoints(points1: Set<Point>, points2: Set<Point>) : Pair<List<Point>, List<Point>> {
        val points1Positions = points1.map { point -> point.position  }.toCollection(mutableSetOf())
        val points2Positions = points2.map { point -> point.position  }.toCollection(mutableSetOf())
        val wire1 = points1
            .filter { point -> points2Positions.contains(point.position) }
            .toCollection(mutableListOf())
        val wire2 = points2
            .filter { point -> points1Positions.contains(point.position) }
            .toCollection(mutableListOf())
        return Pair(wire1, wire2)
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