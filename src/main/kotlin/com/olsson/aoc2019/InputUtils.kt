package com.olsson.aoc2019

import java.io.File
import java.lang.IllegalArgumentException

internal object InputUtils {

    fun getFromResources(file : String) : File {
        val uri = InputUtils.javaClass.classLoader.getResource (file)?.toURI()
                ?: throw IllegalArgumentException("File was not found")
        return File(uri)
    }

    fun getLines(file: String) : List<String> {
        return getFromResources(file).readLines().toCollection(arrayListOf())
    }

    fun getString(file : String, separator : String = ",") : String {
        return getFromResources(file).readLines().reduce{a, b -> "$a$separator$b"}
    }

    fun getStrings(file : String, separator : String = ",") : List<String> {
        return getString(file, separator).split(separator)
    }

    fun getInts(file: String) : List<Int> {
        return getStrings(file).map { it.toInt() }
    }

    fun getLongs(file: String) : List<Long> {
        return getStrings(file).map { it.toLong() }
    }

}