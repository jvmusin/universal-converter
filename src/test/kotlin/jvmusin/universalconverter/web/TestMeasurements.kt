package jvmusin.universalconverter.web

import jvmusin.universalconverter.converter.ConversionRule
import jvmusin.universalconverter.converter.MeasurementConverter
import jvmusin.universalconverter.converter.factory.MeasurementConverterFactoryImpl
import jvmusin.universalconverter.number.DoubleNumber
import jvmusin.universalconverter.number.DoubleNumberFactory
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean

@TestConfiguration
class TestMeasurements {
    @Bean
    fun measurementConverter(): MeasurementConverter<DoubleNumber> {
        return MeasurementConverterFactoryImpl(DoubleNumberFactory()).create(
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
