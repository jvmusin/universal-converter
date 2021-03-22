package com.github.jvmusin.universalconverter.converter

import com.github.jvmusin.universalconverter.number.DoubleNumber
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class MeasurementConverterUtilsTests : BehaviorSpec({
    Given("buildConversionGraph") {
        When("всё хорошо") {
            Then("создаёт граф переходов") {
                val rules = listOf(
                    ConversionRule("n16", "n4", 4.0),
                    ConversionRule("n4", "n20", 0.25)
                )
                val graph = MeasurementConverterUtils.buildConversionGraph(rules)
                graph shouldBe mapOf(
                    "n16" to listOf(ConversionRule("n4", "n16", 0.25)),
                    "n4" to listOf(
                        ConversionRule("n16", "n4", 4.0),
                        ConversionRule("n20", "n4", 4.0)
                    ),
                    "n20" to listOf(ConversionRule("n4", "n20", 0.25))
                )
            }
        }
        When("список правил равен null") {
            Then("бросает IllegalArgumentException") {
                shouldThrow<IllegalArgumentException> {
                    MeasurementConverterUtils.buildConversionGraph<DoubleNumber>(null)
                }
            }
        }
        When("список правил содержит null") {
            Then("бросает IllegalArgumentException") {
                val rules = listOf(
                    ConversionRule("a", "b", 2.0),
                    null,
                    ConversionRule("b", "c", 4.0)
                )
                shouldThrow<IllegalArgumentException> {
                    MeasurementConverterUtils.buildConversionGraph(rules)
                }
            }
        }
    }
})
