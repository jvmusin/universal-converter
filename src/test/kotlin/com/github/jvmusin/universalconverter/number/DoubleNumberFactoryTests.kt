package com.github.jvmusin.universalconverter.number

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class DoubleNumberFactoryTests : BehaviorSpec() {
    private val factory = DoubleNumberFactory()

    init {
        Given("единица one") {
            When("когда вызван") {
                Then("возвращает 1/1") {
                    factory.one().value shouldBe 1
                }
            }
        }

        Given("парсинг parse") {
            When("строка null") {
                Then("бросает IllegalArgumentException") {
                    shouldThrow<IllegalArgumentException> { factory.parse(null) }
                }
            }
            When("пустая строка") {
                Then("бросает NumberFormatException") {
                    shouldThrow<NumberFormatException> { factory.parse("") }
                }
            }
            When("строка из точки") {
                Then("бросает NumberFormatException") {
                    shouldThrow<NumberFormatException> { factory.parse(".") }
                }
            }
            When("строка с буквами") {
                Then("бросает NumberFormatException") {
                    shouldThrow<NumberFormatException> {
                        val result = factory.parse("12a3z;")
                        println(result)
                        result
                    }
                }
            }
            When("строка с двумя точками") {
                Then("бросает NumberFormatException") {
                    shouldThrow<NumberFormatException> { factory.parse("123.45.6") }
                }
            }

            When("строка '-1.2'") {
                Then("парсится как '-1.2'") {
                    factory.parse("-1.2").value shouldBe -1.2
                }
            }
            When("строка '123'") {
                Then("парсится как 123") {
                    factory.parse("123").value shouldBe 123
                }
            }
            When("строка '123.'") {
                Then("парсится как 123") {
                    factory.parse("123.").value shouldBe 123
                }
            }
            When("строка '123.45'") {
                Then("парсится как 123.45") {
                    factory.parse("123.45").value shouldBe 123.45
                }
            }
            When("строка '.45'") {
                Then("парсится как 0.45") {
                    factory.parse(".45").value shouldBe 0.45
                }
            }
        }
    }
}