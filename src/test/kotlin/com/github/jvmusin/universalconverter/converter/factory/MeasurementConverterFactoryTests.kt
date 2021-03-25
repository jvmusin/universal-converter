package com.github.jvmusin.universalconverter.converter.factory

import com.github.jvmusin.universalconverter.converter.exception.MeasurementConverterBuildException
import com.github.jvmusin.universalconverter.converter.measurementConverterFactory
import com.github.jvmusin.universalconverter.converter.sampleRules
import com.github.jvmusin.universalconverter.fraction.ComplexFraction
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class MeasurementConverterFactoryTests : BehaviorSpec({
    Given("создание конвертера create") {
        When("правила равны null") {
            Then("бросает MeasurementConverterBuildException") {
                shouldThrow<MeasurementConverterBuildException> {
                    measurementConverterFactory.create(null)
                }
            }
        }
        When("среди правил есть null") {
            Then("бросает MeasurementConverterBuildException") {
                shouldThrow<MeasurementConverterBuildException> {
                    measurementConverterFactory.create(sampleRules + null)
                }
            }
        }
        When("всё хорошо") {
            Then("создаёт корректный конвертер") {
                val converter = measurementConverterFactory.create(sampleRules)
                val result = converter.convertFractions(
                    ComplexFraction(listOf("км"), listOf()),
                    ComplexFraction(listOf("м"), listOf())
                )
                result.value shouldBe 1000
            }
        }
    }
})
