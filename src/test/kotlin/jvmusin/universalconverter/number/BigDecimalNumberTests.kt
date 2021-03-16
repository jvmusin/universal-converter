package jvmusin.universalconverter.number

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import java.math.MathContext

class BigDecimalNumberTests : StringSpec() {
    private val mathContext = MathContext.DECIMAL128
    private val factory = BigDecimalNumberFactory(mathContext)

    private fun create(s: String): BigDecimalNumber = factory.parse(s)
    private fun createEpsString(eps: Int) = "0.${"0".repeat(eps - 1)}1"

    @Suppress("SameParameterValue")
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
        "1e-100 положительно" {
            createEps(100).isPositive.shouldBeTrue()
        }
        "0 не положителен" {
            create("0").isPositive.shouldBeFalse()
        }
        "-3 не положительно" {
            create("-3").isPositive.shouldBeFalse()
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
