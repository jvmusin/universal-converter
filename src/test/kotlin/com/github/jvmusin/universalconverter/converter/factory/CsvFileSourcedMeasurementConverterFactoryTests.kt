package com.github.jvmusin.universalconverter.converter.factory

import com.github.jvmusin.universalconverter.converter.conversionGraphFactory
import com.github.jvmusin.universalconverter.converter.exception.MeasurementConverterBuildException
import com.github.jvmusin.universalconverter.converter.weightFactory
import com.github.jvmusin.universalconverter.fraction.ComplexFraction
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.engine.spec.tempfile
import io.kotest.matchers.shouldBe
import java.nio.file.Path

class CsvFileSourcedMeasurementConverterFactoryTests : BehaviorSpec() {
    private val converterFactory =
        CsvFileSourcedMeasurementConverterFactory(weightFactory, conversionGraphFactory)

    private fun writeRules(vararg rules: String): Path {
        return tempfile("rules", ".csv").apply { writeText(rules.joinToString("\n")) }.toPath()
    }

    private fun createConverter(vararg rules: String) = converterFactory.create(writeRules(*rules))

    init {
        Given("создание конвертера create") {
            When("всё хорошо") {
                Then("создаёт конвертер") {
                    val converter = createConverter(
                        "м,см,100",
                        "мм,м,0.001",
                        "км,м,1000",
                        "час,мин,60",
                        "мин,с,60"
                    )
                    val result = converter.convertFractions(
                        ComplexFraction(listOf("км"), listOf()),
                        ComplexFraction(listOf("мм"), listOf())
                    )
                    result.value shouldBe 1_000_000
                }
            }
            When("есть пустые строки") {
                Then("игнорирует их и создаёт конвертер") {
                    val converter = createConverter(
                        "",
                        "м,см,100",
                        "мм,м,0.001",
                        "  ",
                        "км,м,1000",
                        "час,мин,60",
                        "мин,с,60",
                        "\t   \t ",
                        "\t",
                    )
                    val result = converter.convertFractions(
                        ComplexFraction(listOf("км"), listOf()),
                        ComplexFraction(listOf("мм"), listOf())
                    )
                    result.value shouldBe 1_000_000
                }
            }
            When("есть пустой токен") {
                Then("бросает MeasurementConverterBuildException") {
                    shouldThrow<MeasurementConverterBuildException> {
                        createConverter(
                            "м,см,100",
                            "мм,м,0.001",
                            ",м,1000",
                            "час,мин,60",
                            "мин,с,60"
                        )
                    }
                }
            }
            When("вместо трёх токенов два") {
                Then("бросает MeasurementConverterBuildException") {
                    shouldThrow<MeasurementConverterBuildException> {
                        createConverter(
                            "м,см,100",
                            "мм,м,0.001",
                            "м,1000",
                            "час,мин,60",
                            "мин,с,60"
                        )
                    }
                }
            }
            When("вместо трёх токенов четыре") {
                Then("бросает MeasurementConverterBuildException") {
                    shouldThrow<MeasurementConverterBuildException> {
                        createConverter(
                            "м,см,100",
                            "мм,м,0.001",
                            "км,м,1000,qq",
                            "час,мин,60",
                            "мин,с,60"
                        )
                    }
                }
            }
            When("не получается спарсить вес") {
                Then("бросает MeasurementConverterBuildException") {
                    shouldThrow<MeasurementConverterBuildException> {
                        createConverter(
                            "м,см,100q",
                            "мм,м,0.001",
                            "км,м,1000",
                            "час,мин,60",
                            "мин,с,60"
                        )
                    }
                }
            }
        }
    }
}
