package com.github.jvmusin.universalconverter.converter

import com.github.jvmusin.universalconverter.converter.factory.MeasurementConverterFactoryImpl
import com.github.jvmusin.universalconverter.number.DoubleNumber
import com.github.jvmusin.universalconverter.number.DoubleNumberFactory
import com.github.jvmusin.universalconverter.number.Number

@Suppress("TestFunctionName")
fun ConversionRule(
    bigPiece: String,
    smallPiece: String,
    weight: Double
): ConversionRule<DoubleNumber> {
    return ConversionRule(bigPiece, smallPiece, DoubleNumber(weight))
}

val sampleRules
    get() = listOf(
        ConversionRule("м", "см", 100.0),
        ConversionRule("мм", "м", 0.001),
        ConversionRule("км", "м", 1000.0),
        ConversionRule("час", "мин", 60.0),
        ConversionRule("мин", "с", 60.0),
    )

fun <T : Number<T>> List<ConversionRule<T>>.toConversionGraph(): Map<String, List<ConversionRule<T>>> =
    MeasurementConverterUtils.buildConversionGraph(this)

val sampleConverter: MeasurementConverter<DoubleNumber>
    get() = MeasurementConverterFactoryImpl(DoubleNumberFactory()).create(sampleRules)

val sampleConversionGraph: MutableMap<String, MutableList<ConversionRule<DoubleNumber>>>
    get() = MeasurementConverterUtils.buildConversionGraph(sampleRules)