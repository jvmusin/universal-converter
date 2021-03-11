package jvmusin.universalconverter

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import jvmusin.universalconverter.exception.ConversionException
import jvmusin.universalconverter.exception.MismatchedDimensionalityException
import jvmusin.universalconverter.exception.NoSuchMeasurementException

class FailingConverterTests : StringSpec({
    val converter = sampleConverter

    "Метры в секунды бросает ConversionException" {
        val from = ComplexFraction(listOf("м"), emptyList())
        val to = ComplexFraction(listOf("с"), emptyList())
        shouldThrow<ConversionException> { converter.convert(from, to) }
    }

    "Метры в луноходы и наоборот бросает NoSuchMeasurementException" {
        val from = ComplexFraction(listOf("м"), emptyList())
        val to = ComplexFraction(listOf("луноход"), emptyList())
        shouldThrow<NoSuchMeasurementException> { converter.convert(from, to) }
        shouldThrow<NoSuchMeasurementException> { converter.convert(to, from) }
    }

    "Секунда на метр в метр на секунду бросает ConversionException" {
        val from = ComplexFraction(listOf("с"), listOf("м"))
        val to = ComplexFraction(listOf("м"), listOf("с"))
        shouldThrow<ConversionException> { converter.convert(from, to) }
    }

    "Различные длины в числителе и знаменателе бросает MismatchedDimensionalityException, обёрнутый в ConversionException" {
        val from = ComplexFraction(listOf("м"), listOf("с"))
        val to = ComplexFraction(listOf("м", "м"), listOf("с"))
        val e = shouldThrow<ConversionException> { converter.convert(from, to) }
        e.cause.shouldNotBeNull()::class shouldBe MismatchedDimensionalityException::class
    }
})