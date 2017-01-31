package com.rubengees.mlp

import java.net.URI
import java.nio.file.Path
import java.nio.file.Paths

/**
 * TODO: Describe class
 *
 * @author Ruben Gees
 */

fun List<Double>.normalize(min: Double, max: Double): List<Double> {
    return this.map {
        it.normalize(min, max)
    }
}

fun List<Double>.denormalize(min: Double, max: Double): List<Double> {
    return this.map {
        it.denormalize(min, max)
    }
}

fun Double.normalize(min: Double, max: Double): Double {
    if (min == max) {
        return this
    }

    return (this - min) / (max - min)
}

fun Double.denormalize(min: Double, max: Double): Double {
    if (min == max) {
        return this
    }

    return this * (max - min) + min
}

fun <K, V> Map<K, V>.invert(): Map<V, K> {
    return mapOf(*this.map { it.value to it.key }.toTypedArray())
}