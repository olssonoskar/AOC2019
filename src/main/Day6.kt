package main

import java.io.File

fun main() {
    val input : List<Pair<String, String>> = File("src\\resources\\day6.txt").readLines()
        .map {orbit ->
            val planets = orbit.split(")")
            Pair(planets[0], planets[1])
        }
        .toCollection(mutableListOf())
    val day6 = Day6()
    day6.buildMap(input)
    println(day6.calcOrbits())
    println(day6.calcOrbitalTransfer())
}

class Day6 {

    private val com = "COM"
    private val root = Planet(com, "", mutableListOf())
    private val existingPlanets = mutableSetOf(com)

    fun buildMap(orbits: List<Pair<String, String>>) {
        var index = 0
        while (existingPlanets.size - 1 < orbits.size) {
            val candidate = orbits[index]
            //Since we do not know if parent exist, loop until it does and planet not added
            if(existingPlanets.contains(candidate.first) && !existingPlanets.contains(candidate.second)) {
                insertPlanet(candidate.second, candidate.first)
                existingPlanets.add(candidate.second)
            }
            index = (index + 1) % orbits.size
        }
    }

    private fun insertPlanet(newPlanet: String, orbiting: String) {
        if (orbiting == root.name) {
            root.orbiters.add(Planet(newPlanet, orbiting, mutableListOf()))
            return
        }
        val parent = findPlanet(orbiting, root)
        parent?.orbiters?.add(Planet(newPlanet, orbiting, mutableListOf()))
            ?: println("Planet $orbiting was not found")
    }

    private fun findPlanet(searched: String, root: Planet): Planet? {
        for (planet in root.orbiters) {
            if (planet.name == searched) {
                return planet
            } else {
                val depthSearch = findPlanet(searched, planet)
                if (depthSearch != null) {
                    return depthSearch
                }
            }
        }
        return null
    }

    fun calcOrbits() : Int {
        return calcOrbits(root, 0)
    }

    private fun calcOrbits(planet: Planet, level: Int) : Int {
        var result = level
        for (childPlanet in planet.orbiters) {
            result += calcOrbits(childPlanet, level + 1)
        }
        return result
    }

    fun calcOrbitalTransfer(): String {
        val path1 = findPlanetPath("YOU", root)?.split(",")
        val path2 = findPlanetPath("SAN", root)?.split(",")
        if (path1 != null && path2 != null) {
            val change = indexOfChange(path1, path2)
            return (path1.size + path2.size - (change * 2)).toString()
        }
        return "not found"
    }

    private fun findPlanetPath(searched: String, parent: Planet): String? {
        for (planet in parent.orbiters) {
            if (planet.name == searched) {
                return parent.name
            } else {
                val childName = findPlanetPath(searched, planet)
                if (childName != null) {
                    return "${parent.name}, $childName"
                }
            }
        }
        return null
    }

    private fun indexOfChange(first: List<String>, second: List<String>): Int {
        var index = 0
        while (first[index] == second[index]) {
            index++
        }
        return index
    }

    private class Planet (
        val name: String,
        val orbiting: String,
        val orbiters: MutableList<Planet>
    )
}