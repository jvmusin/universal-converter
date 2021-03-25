package com.github.jvmusin.universalconverter.web

import com.github.jvmusin.universalconverter.converter.sampleMeasurementConverter
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean

@TestConfiguration
class TestMeasurements {
    @Bean
    fun measurementConverter() = sampleMeasurementConverter
}
