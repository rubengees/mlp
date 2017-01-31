package com.rubengees.mlp

/**
 * TODO: Describe class
 *
 * @author Ruben Gees
 */
abstract class Layer(private val inputAmount: Int, private val outputAmount: Int) {

    val neurons: List<Neuron>

    init {
        neurons = Array<Neuron>(outputAmount, { Neuron(inputAmount) }).toList()
    }

    fun feedForward(input: List<Double>): List<Double> {
        return Array<Double>(outputAmount, { neurons[it].feedForward(input) }).toList()
    }

    fun propagateBack(deltas: List<Double>, previousTransfers: List<Double>) {
        deltas.forEachIndexed { index, delta ->
            neurons[index].propagateBack(delta, previousTransfers)
        }
    }
}