package com.github.jvmusin.universalconverter.converter

import com.github.jvmusin.universalconverter.converter.factory.MeasurementConverterFactoryImpl
import com.github.jvmusin.universalconverter.converter.graph.ConversionGraphFactory
import com.github.jvmusin.universalconverter.converter.network.ConversionNetworkFactory
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

val doubleNumberFactory = DoubleNumberFactory()
val conversionGraphFactory =
    ConversionGraphFactory()
val conversionNetworkFactory = ConversionNetworkFactory()

val sampleRules
    get() = listOf(
        ConversionRule("м", "см", 100.0),
        ConversionRule("мм", "м", 0.001),
        ConversionRule("км", "м", 1000.0),
        ConversionRule("час", "мин", 60.0),
        ConversionRule("мин", "с", 60.0),
    )

fun <T : Number<T>> List<ConversionRule<T>>.toConversionGraph(): Map<String, List<ConversionRule<T>>> =
    conversionGraphFactory.create(this)

val sampleConverter: MeasurementConverter<DoubleNumber>
    get() = MeasurementConverterFactoryImpl(
        doubleNumberFactory,
        conversionNetworkFactory,
        conversionGraphFactory
    ).create(sampleRules)

val sampleConversionGraph: Map<String, List<ConversionRule<DoubleNumber>>>
    get() = sampleRules.toConversionGraph()
