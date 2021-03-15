package jvmusin.universalconverter.number

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import java.math.BigInteger.ONE

class BigIntFractionNumberFactoryTests : BehaviorSpec() {
    private val factory = BigIntFractionNumberFactory()

    init {
        Given("единица one") {
            When("когда вызван") {
                Then("возвращает 1/1") {
                    factory.one() shouldBe BigIntFractionNumber(ONE, ONE)
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
                    shouldThrow<NumberFormatException> { factory.parse("12a3") }
                }
            }
            When("строка с двумя точками") {
                Then("бросает NumberFormatException") {
                    shouldThrow<NumberFormatException> { factory.parse("123.45.6") }
                }
            }

            When("отрицательное число") {
                Then("парсится как надо") {
                    factory.parse("-1.234") shouldBe BigIntFractionNumber((-1234).toBigInteger(), 1000.toBigInteger())
                }
            }
            When("строка '123'") {
                Then("парсится как 123/1") {
                    factory.parse("123") shouldBe BigIntFractionNumber(123.toBigInteger(), 1.toBigInteger())
                }
            }
            When("строка '123.'") {
                Then("парсится как 123/1") {
                    factory.parse("123.") shouldBe BigIntFractionNumber(123.toBigInteger(), 1.toBigInteger())
                }
            }
            When("строка '123.45'") {
                Then("парсится как 12345/100") {
                    factory.parse("123.45") shouldBe BigIntFractionNumber(12345.toBigInteger(), 100.toBigInteger())
                }
            }
            When("строка '.45'") {
                Then("парсится как 45/100") {
                    factory.parse(".45") shouldBe BigIntFractionNumber(45.toBigInteger(), 100.toBigInteger())
                }
            }
        }
    }
}
