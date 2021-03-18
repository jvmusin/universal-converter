package com.github.jvmusin.universalconverter.converter

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import com.github.jvmusin.universalconverter.converter.factory.MeasurementConverterFactoryImpl
import com.github.jvmusin.universalconverter.fraction.ComplexFraction
import com.github.jvmusin.universalconverter.number.BigDecimalNumber
import com.github.jvmusin.universalconverter.number.BigDecimalNumberFactory
import java.math.BigDecimal.ONE
import java.math.MathContext

class MeasurementConverterStressTest : StringSpec({
    "При длинной цепочке умножений или делений на 10, точность не теряется" {
        val n = 50_000
        val mc = MathContext.DECIMAL128
        val numberFactory = BigDecimalNumberFactory(mc)
        val measurementConverterFactory = MeasurementConverterFactoryImpl(numberFactory)
        val rules = List(n - 1) {
            ConversionRule("n${it + 1}", "n${it + 2}", numberFactory.parse("0.1"))
        }
        val converter = measurementConverterFactory.create(rules)
        val first = ComplexFraction(listOf("n1"), listOf())
        val last = ComplexFraction(listOf("n$n"), listOf())
        converter.convert(last, first) shouldBe BigDecimalNumber(ONE.movePointRight(n - 1), mc)
        converter.convert(first, last) shouldBe BigDecimalNumber(ONE.movePointLeft(n - 1), mc)
    }
})
