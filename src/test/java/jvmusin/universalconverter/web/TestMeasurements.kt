package jvmusin.universalconverter.web

import jvmusin.universalconverter.ConversionRule
import jvmusin.universalconverter.MeasurementConverter
import jvmusin.universalconverter.MeasurementConverterFactory
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean

@TestConfiguration
class TestMeasurements {
    @Bean
    fun measurementConverter(factory: MeasurementConverterFactory): MeasurementConverter<String> = factory.create(
        listOf(
            ConversionRule("м", "см", 100.0),
            ConversionRule("мм", "м", 0.001),
            ConversionRule("км", "м", 1000.0),
            ConversionRule("час", "мин", 60.0),
            ConversionRule("мин", "с", 60.0)
        )
    )
}
