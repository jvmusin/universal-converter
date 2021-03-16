package jvmusin.universalconverter.converter.factory

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import jvmusin.universalconverter.converter.exception.MeasurementConverterBuildException
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
