package com.rubengees.mlp

import java.util.*

/**
 * TODO: Describe class
 *
 * @author Ruben Gees
 */
class Neuron(private val inputAmount: Int) {

    private companion object {
        private const val eta = 0.01
    }

    var weights: List<Double>
        private set

    var lastInput = Array<Double>(inputAmount, { Double.MIN_VALUE }).toList()
        private set
    var lastActivation = Double.MIN_VALUE
        private set
    var lastTransfer = Double.MIN_VALUE
        private set

    init {
        weights = Array<Double>(inputAmount, { (Random().nextDouble() - 0.5) * 4.0 }).toList()
    }

    fun feedForward(input: List<Double>): Double {
        if (input.size != inputAmount) {
            throw IllegalArgumentException("The input must have a size of $inputAmount. Actual size: ${input.size}")
        }

        lastInput = input
        lastActivation = input.mapIndexed { index, value -> value * weights[index] }.sum()
        lastTransfer = 1.0 / (1.0 + Math.exp(-lastActivation))

        return lastTransfer
    }

    fun propagateBack(delta: Double, previousTransfers: List<Double>) {
        weights = weights.mapIndexed { index, it -> it + (-eta * delta * previousTransfers[index]) }
    }
}