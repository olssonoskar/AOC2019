package com.olsson.aoc2019

import java.io.File
import java.lang.IllegalArgumentException

internal object Utils {

    fun getFromResources(file : String) : File {
        val uri = Utils.javaClass.classLoader.getResource (file)?.toURI()
                ?: throw IllegalArgumentException("File was not found")
        return File(uri)
    }

    fun getLines(file: String) : List<String> {
        return getFromResources(file).readLines().toCollection(arrayListOf())
    }

    fun getAsString(file : String, separator : String = ",") : String {
        return getFromResources(file).readLines().reduce{a, b -> "$a$separator$b"}
    }

    fun getAsStringList(file : String, separator : String = ",") : List<String> {
        return getAsString(file, separator).split(separator)
    }

    fun getAsIntList(file: String) : List<Int> {
        return getAsStringList(file).map { it.toInt() }
    }

}