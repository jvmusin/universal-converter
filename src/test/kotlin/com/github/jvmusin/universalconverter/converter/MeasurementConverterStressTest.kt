package com.github.jvmusin.universalconverter.converter

import com.github.jvmusin.universalconverter.converter.factory.MeasurementConverterFactory
import com.github.jvmusin.universalconverter.converter.graph.ConversionGraphFactory
import com.github.jvmusin.universalconverter.fraction.ComplexFraction
import com.github.jvmusin.universalconverter.number.BigDecimalNumber
import com.github.jvmusin.universalconverter.number.BigDecimalNumberFactory
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.math.BigDecimal.ONE
import java.math.MathContext

class MeasurementConverterStressTest : StringSpec({
    "При длинной цепочке умножений или делений на 10 точность не теряется" {
        val n = 50_000
        val mc = MathContext.DECIMAL128
        val weightFactory = BigDecimalNumberFactory(mc)
        val measurementConverterFactory =
            MeasurementConverterFactory(weightFactory, ConversionGraphFactory(weightFactory))
        val rules = List(n - 1) {
            ConversionRule("n${it + 1}", "n${it + 2}", weightFactory.parse("0.1"))
        }
        val converter = measurementConverterFactory.create(rules)
        val first = ComplexFraction(listOf("n1"), listOf())
        val last = ComplexFraction(listOf("n$n"), listOf())
        converter.convertFractions(last, first) shouldBe
                BigDecimalNumber(ONE.movePointRight(n - 1), mc)
        converter.convertFractions(first, last) shouldBe
                BigDecimalNumber(ONE.movePointLeft(n - 1), mc)
    }
})
