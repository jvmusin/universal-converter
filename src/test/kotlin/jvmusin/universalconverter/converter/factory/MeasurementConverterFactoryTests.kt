package jvmusin.universalconverter.converter.factory

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveCauseInstanceOf
import jvmusin.universalconverter.converter.ConversionRule
import jvmusin.universalconverter.converter.exception.MeasurementConverterBuildException
import jvmusin.universalconverter.converter.network.exception.ConversionNetworkBuildException
import jvmusin.universalconverter.converter.sampleRules
import jvmusin.universalconverter.fraction.ComplexFraction
import jvmusin.universalconverter.number.DoubleNumberFactory

class MeasurementConverterFactoryTests : BehaviorSpec() {
    private val converterFactory = MeasurementConverterFactoryImpl(DoubleNumberFactory())

    init {
        Given("создание конвертера create") {
            When("правила равны null") {
                Then("бросает MeasurementConverterBuildException") {
                    shouldThrow<MeasurementConverterBuildException> { converterFactory.create(null) }
                }
            }
            When("среди правил есть null") {
                Then("бросает MeasurementConverterBuildException") {
                    shouldThrow<MeasurementConverterBuildException> {
                        converterFactory.create(sampleRules + null)
                    }
                }
            }
            When("есть бесконечно увеличивающийся цикл") {
                Then("бросает MeasurementConverterBuildException с cause типа ConversionNetworkBuildException") {
                    val wrongRule = sampleRules.first().let {
                        ConversionRule(it.bigPiece, it.smallPiece, 32.0)
                    }
                    val rules = sampleRules + wrongRule
                    shouldThrow<MeasurementConverterBuildException> { converterFactory.create(rules) }
                        .shouldHaveCauseInstanceOf<ConversionNetworkBuildException>()
                }
            }
            When("всё хорошо") {
                Then("создаёт корректный конвертер") {
                    val converter = converterFactory.create(sampleRules)
                    val result = converter.convert(
                        ComplexFraction(listOf("км"), listOf()),
                        ComplexFraction(listOf("м"), listOf())
                    )
                    result.value shouldBe 1000
                }
            }
        }
    }
}
