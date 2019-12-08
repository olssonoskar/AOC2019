package main

import java.io.File

fun main() {
    val input : String = File("src\\resources\\day8.txt").readLines().reduce{ a, b -> "$a,$b" }
    val day8 = Day8(25, 6, input)
    println("Part 1 equals ${day8.validate()}")
    day8.decodeImage()
}

class Day8 (
    private val width : Int,
    private val height : Int,
    private val input: String
){
    private val dataLength = this.width * this.height
    private val imageLayers = createLayers()

    private fun createLayers() : List<Layer> {
        var index = 0
        val layers = mutableListOf<Layer>()
        while (index < input.length) {
            layers.add(createLayer(input.substring(index, index+dataLength)))
            index += dataLength
        }
        return layers
    }

    private fun createLayer(layerData: String) : Layer {
        val newLayer = Layer(this.width, this.height)
        layerData.forEachIndexed { index, c ->
            val level = (index / this.width)
            val position = index % this.width
            newLayer.set(Integer.parseInt(c.toString()), level, position)
        }
        return newLayer
    }

    fun decodeImage() {
        val image = Layer(this.width, this.height)
        for (x in 0 until this.width) {
            for (y in 0 until this.height) {
                image.set(getImageColor(x, y), y, x)
            }
        }
        for (y in 0 until this.height) {
            println(image.getRow(y).replace("0", " "))
        }
    }

    fun validate() : Int {
        val mostZeroLayer = getLayerWithMostZero()
        return mostZeroLayer?.checkSum() ?: throw IllegalArgumentException("No element has fewest 0")
    }

    private fun getLayerWithMostZero() : Layer? {
        return imageLayers
            .minBy { layer -> layer.getTotalZero() }
    }

    private fun getImageColor(x: Int, y: Int) : Int {
        return imageLayers.
            filter { layer -> layer.get(y, x) != 2 }
            .map { layer -> layer.get(y, x) }
            .first()

    }

    private class Layer(
        val width: Int,
        height: Int
    ) {
        private val data: Array<IntArray> = Array(height) { IntArray(width) }
        private var totalZero = 0

        fun set(value: Int, level: Int, position: Int) {
            if (value == 0) {
                totalZero += 1
            }
            data[level][position] = value
        }

        fun get(level: Int, position: Int) : Int {
            return data[level][position]
        }

        fun getRow(level: Int) : String {
            return data[level]
                .map { it.toString() }
                .reduce{a ,b -> a + b}
        }

        fun checkSum() : Int {
            var ones = 0
            var twos = 0
            for (level in data) {
                for (number in level)
                    if (number == 1) {
                        ones += 1
                    } else if (number == 2) {
                        twos += 1
                    }
            }
            return ones * twos
        }

        fun getTotalZero() : Int {
            return totalZero
        }

    }
}