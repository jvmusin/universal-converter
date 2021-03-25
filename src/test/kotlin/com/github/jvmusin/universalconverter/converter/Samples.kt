package com.github.jvmusin.universalconverter.converter

import com.github.jvmusin.universalconverter.converter.factory.MeasurementConverterFactory
import com.github.jvmusin.universalconverter.converter.graph.ConversionGraph
import com.github.jvmusin.universalconverter.converter.graph.ConversionGraphFactory
import com.github.jvmusin.universalconverter.converter.graph.WeightedMeasurement
import com.github.jvmusin.universalconverter.number.DoubleNumber
import com.github.jvmusin.universalconverter.number.DoubleNumberFactory
import com.github.jvmusin.universalconverter.number.Number

typealias Weight = DoubleNumber

@Suppress("TestFunctionName")
fun ConversionRule(
    bigPiece: String,
    smallPiece: String,
    weight: Double
): ConversionRule<Weight> {
    return ConversionRule(bigPiece, smallPiece, Weight(weight))
}

val weightFactory = DoubleNumberFactory()
val conversionGraphFactory = ConversionGraphFactory(weightFactory)
val measurementConverterFactory = MeasurementConverterFactory(weightFactory, conversionGraphFactory)

val sampleRules = listOf(
    ConversionRule("м", "см", 100.0),
    ConversionRule("мм", "м", 0.001),
    ConversionRule("км", "м", 1000.0),
    ConversionRule("час", "мин", 60.0),
    ConversionRule("мин", "с", 60.0),
)

fun List<ConversionRule<Weight>>.toConversionGraph(): ConversionGraph<Weight> =
    conversionGraphFactory.create(this)

val sampleMeasurementConverter: MeasurementConverter<Weight> =
    measurementConverterFactory.create(sampleRules)
val sampleConversionGraph: ConversionGraph<Weight> = sampleRules.toConversionGraph()

operator fun <T : Number<T>> ConversionGraph<T>.get(name: String): WeightedMeasurement<T>? =
    getMeasurement(name)
