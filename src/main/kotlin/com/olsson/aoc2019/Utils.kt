package com.olsson.aoc2019

import java.io.File
import java.lang.IllegalArgumentException

internal object Utils {

    fun getFromResources(file : String) : File {
        val uri = Utils.javaClass.classLoader.getResource (file)?.toURI()
                ?: throw IllegalArgumentException("File was not found")
        return File(uri)
    }

}