package jvmusin.universalconverter.number

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.doubles.plusOrMinus
import io.kotest.matchers.doubles.shouldBePositiveInfinity
import io.kotest.matchers.shouldBe

class DoubleNumberTests : StringSpec({
    "3 * 9.1 = 18.3" {
        DoubleNumber(3.0).multiplyBy(DoubleNumber(9.1)).value shouldBe (27.3 plusOrMinus 1e-10)
    }
    "3 / 2 = 1.5" {
        DoubleNumber(3.0).divideBy(DoubleNumber(2.0)) shouldBe DoubleNumber(1.5)
    }
    "3 / 0 = +INF" {
        DoubleNumber(3.0).divideBy(DoubleNumber(0.0)).value.shouldBePositiveInfinity()
    }
    "обратное к 5 = 0.2" {
        DoubleNumber(5.0).inverse() shouldBe DoubleNumber(0.2)
    }
    "обратное к 0 = +INF" {
        DoubleNumber(0.0).inverse().value.shouldBePositiveInfinity()
    }
    "5 положительно" {
        DoubleNumber(5.0).isPositive.shouldBeTrue()
    }
    "1e-30 положительно" {
        DoubleNumber(1e-30).isPositive.shouldBeTrue()
    }
    "0 не положительно" {
        DoubleNumber(0.0).isPositive.shouldBeFalse()
    }
    "Double.MIN_VALUE положительно" {
        DoubleNumber(Double.MIN_VALUE).isPositive.shouldBeTrue()
    }
    "-3 не положительно" {
        DoubleNumber(-3.0).isPositive.shouldBeFalse()
    }
    "13.565 в строку и в дабл даёт то же самое число" {
        val x = 13.565
        DoubleNumber(x).toString().toDouble() shouldBe x
    }
})
