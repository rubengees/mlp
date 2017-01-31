package com.rubengees.mlp

import java.util.*

/**
 * TODO: Describe class
 *
 * @author Ruben Gees
 */
class Mlp(private val inputAmount: Int, private val outputAmount: Int, hiddenNeuronAmount: Int = 4,
          private val minValue: Double = 1.0, private val maxValue: Double = 1.0) {

    private val hiddenLayer = HiddenLayer(inputAmount, hiddenNeuronAmount)
    private val outputLayer = OutputLayer(hiddenNeuronAmount, outputAmount)

    init {
        if (inputAmount <= 0) {
            throw IllegalArgumentException("There must be at least one input. Specified input amount: $inputAmount")
        }

        if (outputAmount <= 0) {
            throw IllegalArgumentException("There must be at least one output. Specified output amount: $outputAmount")
        }
    }

    fun map(input: List<Double>): List<Double> {
        if (input.size != inputAmount) {
            throw IllegalArgumentException("The input must have a size of $inputAmount. Actual size: ${input.size}")
        }

        return outputLayer.feedForward(hiddenLayer.feedForward(input.normalize(minValue, maxValue))).denormalize(minValue, maxValue)
    }

    fun learn(input: Pair<List<Double>, List<Double>>): Double {
        if (input.first.size != inputAmount) {
            throw IllegalArgumentException("The input must have a size of $inputAmount. Actual size: ${input.first.size}")
        }

        if (input.second.size != outputAmount) {
            throw IllegalArgumentException("The trainer values must have a size of $outputAmount. Actual size: " +
                    "${input.second.size}")
        }

        val hiddenResult = hiddenLayer.feedForward(input.first.normalize(minValue, maxValue))
        val outputResult = outputLayer.feedForward(hiddenResult)

        val error = (0.5 * Math.pow(outputResult.mapIndexed { index: Int, result: Double ->
            result - input.second.normalize(minValue, maxValue)[index]
        }.sum(), 2.0))

        val outputDeltas = outputLayer.calculateDeltas(input.second.normalize(minValue, maxValue))
        val hiddenDeltas = hiddenLayer.calculateDeltas(outputDeltas, outputLayer)

        outputLayer.propagateBack(outputDeltas, hiddenResult)
        hiddenLayer.propagateBack(hiddenDeltas, input.first.normalize(minValue, maxValue))

        return error
    }

    fun learnAll(inputs: List<Pair<List<Double>, List<Double>>>): Double {
        return inputs.map { learn(it) }.sum()
    }

    fun learnAllFor(inputs: List<Pair<List<Double>, List<Double>>>, epochs: Int,
                    progressListener: ((epoch: Int, error: Double) -> Unit)? = null): List<Double> {
        val result = ArrayList<Double>(epochs)
        var currentEpoch = 0

        for (i in epochs downTo 0) {
            result += learnAll(inputs)
            currentEpoch++

            progressListener?.invoke(currentEpoch, result.last())
        }

        return result
    }

    fun learnAllUntil(inputs: List<Pair<List<Double>, List<Double>>>, finishedFunction: (Double) -> Boolean,
                      progressListener: ((epoch: Int, error: Double) -> Unit)? = null): List<Double> {
        val result = ArrayList<Double>()
        var currentEpoch = 0

        do {
            result += learnAll(inputs)
            currentEpoch++

            progressListener?.invoke(currentEpoch, result.last())
        } while (!finishedFunction.invoke(result.last()))

        return result
    }
}