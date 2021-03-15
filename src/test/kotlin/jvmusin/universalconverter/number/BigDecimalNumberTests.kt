package jvmusin.universalconverter.number

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import java.math.BigDecimal.ONE
import java.math.MathContext.DECIMAL128

class BigDecimalNumberTests : StringSpec() {
    private val mc = DECIMAL128
    private val maxDifference = ONE.scaleByPowerOfTen(-5)
    private val props = BigDecimalNumberProperties(mc, maxDifference)
    private val factory = BigDecimalNumberFactory(props)

    private fun create(s: String): BigDecimalNumber = factory.parse(s)
    private fun createEpsString(eps: Int) = "0.${"0".repeat(eps - 1)}1"
    private fun createEps(eps: Int) = create(createEpsString(eps))

    init {
        "3 * 9.1 = 18.3" {
            create("3") * create("9.1") shouldBe create("27.3")
        }
        "3 / 2 = 1.5" {
            create("3") / create("2") shouldBe create("1.5")
        }
        "3 / 0 = ArithmeticException" {
            shouldThrow<ArithmeticException> { create("3") / create("0") }
        }
        "обратное к 5 = 0.2" {
            create("5").inverse() shouldBe create("0.2")
        }
        "обратное к 0 = ArithmeticException" {
            shouldThrow<ArithmeticException> { create("0").inverse() }
        }
        "0 примерно равно 1e-5" {
            create("0").isNearlyEqualTo(createEps(5)).shouldBeTrue()
        }
        "0 не примерно равно 1e-4" {
            create("0").isNearlyEqualTo(createEps(4)).shouldBeFalse()
        }
        "1e-4 примерно положительно" {
            createEps(4).isNearlyPositive.shouldBeTrue()
        }
        "1e-5 не примерно положительно" {
            createEps(5).isNearlyPositive.shouldBeFalse()
        }
        "-3 не примерно положительно" {
            create("-3").isNearlyPositive.shouldBeFalse()
        }
        "1e-1000 + '234' в строку возвращает всё число" {
            val s = createEpsString(1000) + "234"
            create(s).toString() shouldBe s
        }
        "234e1000 в строку возвращает всё число" {
            val x = create("234") * create("1" + "0".repeat(1000))
            x.toString() shouldBe "234" + "0".repeat(1000)
        }
    }
}
