package jvmusin.universalconverter.converter

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import jvmusin.universalconverter.number.DoubleNumber

class MeasurementConverterUtilsTests : BehaviorSpec({
    Given("buildConversionGraph") {
        When("всё хорошо") {
            Then("создаёт граф переходов") {
                val rules = listOf(
                    ConversionRule("n3", "n2", 2.0),
                    ConversionRule("n2", "n1", 4.0)
                )
                val graph = MeasurementConverterUtils.buildConversionGraph(rules)
                graph shouldBe mapOf(
                    "n1" to listOf(ConversionRule("n1", "n2", 0.25)),
                    "n2" to listOf(
                        ConversionRule("n2", "n1", 4.0),
                        ConversionRule("n2", "n3", 0.5)
                    ),
                    "n3" to listOf(ConversionRule("n3", "n2", 2.0))
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
                    ConversionRule("n3", "n2", 2.0),
                    null,
                    ConversionRule("n2", "n1", 4.0)
                )
                shouldThrow<IllegalArgumentException> {
                    MeasurementConverterUtils.buildConversionGraph(rules)
                }
            }
        }
    }
})
