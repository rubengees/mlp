package com.rubengees.mlp

import java.util.*

/**
 * TODO: Describe class
 *
 * @author Ruben Gees
 */
class OutputLayer(inputAmount: Int, outputAmount: Int) : Layer(inputAmount, outputAmount) {

    fun calculateDeltas(trainerValues: List<Double>): List<Double> {
        if (trainerValues.size != neurons.size) {
            throw IllegalArgumentException("The input must have a size of ${neurons.size}. Actual size: " +
                    "${trainerValues.size}")
        }

        return neurons.mapIndexed { index: Int, neuron: Neuron ->
            val g = Math.exp(neuron.lastActivation) / Math.pow(Math.exp(neuron.lastActivation) + 1, 2.0)
            val delta = g * (neuron.lastTransfer - trainerValues[index])

            delta
        }
    }
}