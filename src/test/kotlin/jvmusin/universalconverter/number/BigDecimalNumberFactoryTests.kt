package jvmusin.universalconverter.number

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import java.math.BigDecimal
import java.math.BigDecimal.ONE
import java.math.MathContext.DECIMAL128

class BigDecimalNumberFactoryTests : BehaviorSpec() {
    private val mc = DECIMAL128
    private val maxDifference = ONE.scaleByPowerOfTen(-5)
    private val props = BigDecimalNumberProperties(mc, maxDifference)
    private val factory = BigDecimalNumberFactory(props)

    init {
        Given("one") {
            When("вызван") {
                Then("возвращает 1") {
                    factory.one() shouldBe BigDecimalNumber(ONE, props)
                }
            }
        }

        Given("парсинг parse") {

            fun parse(s: String?): BigDecimalNumber = factory.parse(s)

            When("строка null") {
                Then("бросает IllegalArgumentException") {
                    shouldThrow<IllegalArgumentException> { parse(null) }
                }
            }
            When("пустая строка") {
                Then("бросает NumberFormatException") {
                    shouldThrow<NumberFormatException> { parse("") }
                }
            }
            When("строка из точки") {
                Then("бросает NumberFormatException") {
                    shouldThrow<NumberFormatException> { parse(".") }
                }
            }
            When("строка с буквами") {
                Then("бросает NumberFormatException") {
                    shouldThrow<NumberFormatException> { parse("12a3") }
                }
            }
            When("строка с двумя точками") {
                Then("бросает NumberFormatException") {
                    shouldThrow<NumberFormatException> { parse("123.45.6") }
                }
            }

            When("отрицательное число") {
                Then("парсится как надо") {
                    parse("-1.234") shouldBe BigDecimalNumber(BigDecimal("-1.234"), props)
                }
            }
            When("строка '123'") {
                Then("парсится как 123/1") {
                    parse("123") shouldBe BigDecimalNumber(123.toBigDecimal(), props)
                }
            }
            When("строка '123.'") {
                Then("парсится как 123/1") {
                    parse("123.") shouldBe BigDecimalNumber(123.toBigDecimal(), props)
                }
            }
            When("строка '123.45'") {
                Then("парсится как 12345/100") {
                    parse("123.45") shouldBe BigDecimalNumber(BigDecimal("123.45"), props)
                }
            }
            When("строка '.45'") {
                Then("парсится как 45/100") {
                    parse(".45") shouldBe BigDecimalNumber(BigDecimal("0.45"), props)
                }
            }
        }
    }
}
