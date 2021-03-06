@file:JvmName("Benchmark")

package com.grappenmaker.aoc2021

import java.io.File
import kotlin.system.measureNanoTime

fun main(args: Array<String>) {
    // Inform user
    if (args.isEmpty()) {
        println("Usage: Benchmark <day> [amount] [inputFile]")
        return
    }

    // Get puzzle index
    val index = args.first().runCatching { toInt() }.getOrNull() ?: run {
        println("Invalid day number/index \"${args.first()}\"")
        return
    }

    // Get benchmark samples
    val amount = args.getOrNull(1)?.runCatching { toInt() }?.onFailure {
        println("Invalid sample amount \"${args[1]}\"")
        return
    }?.getOrNull() ?: 100

    println("Running day $index $amount times.")

    // Create context
    val solution = Solution(index, args.getOrNull(2)?.let { File(it) })

    // Run it
    val runtimes = mutableListOf<Long>()
    for (i in 1..amount) {
        runtimes.add(measureNanoTime { solution.run() } / 1000)
    }

    val measurement = "microseconds"

    val sum = runtimes.sum()
    println("Took $sum $measurement to run $amount times")

    val sorted = runtimes.sorted()
    println("Min: ${sorted.first()} $measurement")
    println("Max: ${sorted.last()} $measurement")
    println("Avg: ${sum.toDouble() / runtimes.size} $measurement")

    val mid = runtimes.size / 2
    val median = sorted[mid] + if (sorted.size % 2 != 0) sorted[mid - 1] else 0
    println("Median: $median $measurement")

    val mode = sorted.groupingBy { it }.eachCount().maxByOrNull { it.value }!!.key
    println("Mode: $mode $measurement")
}