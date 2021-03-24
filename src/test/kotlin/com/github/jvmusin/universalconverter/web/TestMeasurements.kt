package com.github.jvmusin.universalconverter.web

import com.github.jvmusin.universalconverter.converter.ConversionRule
import com.github.jvmusin.universalconverter.converter.MeasurementConverter
import com.github.jvmusin.universalconverter.converter.factory.MeasurementConverterFactoryImpl
import com.github.jvmusin.universalconverter.converter.graph.ConversionGraphFactory
import com.github.jvmusin.universalconverter.converter.network.ConversionNetworkFactory
import com.github.jvmusin.universalconverter.number.DoubleNumber
import com.github.jvmusin.universalconverter.number.DoubleNumberFactory
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean

@TestConfiguration
class TestMeasurements {
    @Bean
    fun measurementConverter(): MeasurementConverter<DoubleNumber> {
        return MeasurementConverterFactoryImpl(
            DoubleNumberFactory(),
            ConversionNetworkFactory(),
            ConversionGraphFactory()
        ).create(
            listOf(
                ConversionRule("м", "см", 100.0),
                ConversionRule("мм", "м", 0.001),
                ConversionRule("км", "м", 1000.0),
                ConversionRule("час", "мин", 60.0),
                ConversionRule("мин", "с", 60.0)
            )
        )
    }
}
