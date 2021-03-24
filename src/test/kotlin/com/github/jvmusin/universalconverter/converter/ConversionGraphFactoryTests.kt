package com.github.jvmusin.universalconverter.converter

import com.github.jvmusin.universalconverter.converter.graph.ConversionGraphFactory
import com.github.jvmusin.universalconverter.number.DoubleNumber
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class ConversionGraphFactoryTests : BehaviorSpec({

    val factory = ConversionGraphFactory()

    Given("create") {
        When("всё хорошо") {
            Then("создаёт граф переходов") {
                val rules = listOf(
                    ConversionRule("n16", "n4", 4.0),
                    ConversionRule("n4", "n20", 0.25)
                )
                val graph = factory.create(rules)
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
                shouldThrow<IllegalArgumentException> { factory.create<DoubleNumber>(null) }
            }
        }
        When("список правил содержит null") {
            Then("бросает IllegalArgumentException") {
                val rules = listOf(
                    ConversionRule("a", "b", 2.0),
                    null,
                    ConversionRule("b", "c", 4.0)
                )
                shouldThrow<IllegalArgumentException> { factory.create(rules) }
            }
        }
        When("существует правило с коэффициентом 0") {
            Then("бросает ...") {
                // TODO fix the test
                val graph = factory.create(listOf(ConversionRule("a", "b", 0.0)))
            }
        }
    }
})
