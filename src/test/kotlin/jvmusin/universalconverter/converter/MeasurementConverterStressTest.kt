package jvmusin.universalconverter.converter

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import jvmusin.universalconverter.converter.factory.MeasurementConverterFactoryImpl
import jvmusin.universalconverter.fraction.ComplexFraction
import jvmusin.universalconverter.number.BigDecimalNumber
import jvmusin.universalconverter.number.BigDecimalNumberFactory
import jvmusin.universalconverter.number.BigDecimalNumberProperties
import java.math.BigDecimal.ONE
import java.math.MathContext

class MeasurementConverterStressTest : StringSpec({
    "При длинной цепочке умножений или делений на 10, точность не теряется" {
        val n = 200_000
        val props = BigDecimalNumberProperties(MathContext(150), ONE.movePointLeft(20))
        val numberFactory = BigDecimalNumberFactory(props)
        val measurementConverterFactory = MeasurementConverterFactoryImpl(numberFactory)
        val rules = (1 until n).map { ConversionRule("n$it", "n${it + 1}", numberFactory.parse("0.1")) }
        val converter = measurementConverterFactory.create(rules)
        val first = ComplexFraction(listOf("n1"), listOf())
        val last = ComplexFraction(listOf("n$n"), listOf())
        converter.convert(last, first) shouldBe BigDecimalNumber(ONE.movePointRight(n - 1), props)
        converter.convert(first, last) shouldBe BigDecimalNumber(ONE.movePointLeft(n - 1), props)
    }
})
