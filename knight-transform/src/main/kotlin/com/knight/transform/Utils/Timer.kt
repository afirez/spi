package com.knight.transform.Utils

import java.util.concurrent.TimeUnit

object Timer {
    val records = HashMap<String, Long>()


    fun start(key: String) {
        records[key] = System.nanoTime()
    }


    fun stop(key: String) {
        records[key]?.let {
            println("\n $key -->COST: ${TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - it)} ms \n")
        }
    }

    fun reset() {
        records.clear()
    }
}