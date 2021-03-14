package jvmusin.universalconverter.converter

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.doubles.plusOrMinus
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveCauseOfType
import jvmusin.universalconverter.converter.exception.ConversionException
import jvmusin.universalconverter.converter.exception.MismatchedDimensionalityException
import jvmusin.universalconverter.converter.exception.NoSuchMeasurementException
import jvmusin.universalconverter.fraction.ComplexFraction

class MeasurementConverterTests : StringSpec() {
    private val converter = sampleConverter

    private fun convert(from: ComplexFraction<String>, to: ComplexFraction<String>) = converter.convert(from, to).value

    init {
        "Пример из условия работает" {
            val from = ComplexFraction(listOf("м"), listOf("с"))
            val to = ComplexFraction(listOf("км"), listOf("час"))
            convert(from, to) shouldBe 3.6
        }

        "Обратный пример из условия работает" {
            val from = ComplexFraction(listOf("км"), listOf("час"))
            val to = ComplexFraction(listOf("м"), listOf("с"))
            convert(from, to) shouldBe 1 / 3.6
        }

        "Километры в метры возвращает 1000" {
            val from = ComplexFraction(listOf("км"), emptyList())
            val to = ComplexFraction(listOf("м"), emptyList())
            convert(from, to) shouldBe 1000
        }

        "Метры в километры возвращает 1/1000" {
            val from = ComplexFraction(listOf("м"), emptyList())
            val to = ComplexFraction(listOf("км"), emptyList())
            convert(from, to) shouldBe 1.0 / 1000
        }

        "Секунда на метр в секунду на метр возвращает 1" {
            val from = ComplexFraction(listOf("с"), listOf("м"))
            val to = ComplexFraction(listOf("с"), listOf("м"))
            convert(from, to) shouldBe 1
        }

        "Минута в квадрате в секунду в квадрате возвращает 60*60" {
            val from =
                ComplexFraction(listOf("мин", "мин"), emptyList())
            val to = ComplexFraction(listOf("с", "с"), emptyList())
            convert(from, to) shouldBe 60 * 60
        }

        "Секунда в квадрате в минуту в квадрате возвращает 1/60/60" {
            val from = ComplexFraction(listOf("с", "с"), emptyList())
            val to =
                ComplexFraction(listOf("мин", "мин"), emptyList())
            convert(from, to) shouldBe 1.0 / 60 / 60
        }

        "Неравные размерности по обеим сторонам работают" {
            val from = ComplexFraction(
                listOf("с", "мм", "м"),
                listOf("м", "мм")
            )
            val to = ComplexFraction(listOf("с", "м"), listOf("км"))
            convert(from, to) shouldBe 1000
        }

        "Из ничего в ничего работает" {
            val from =
                ComplexFraction<String>(emptyList(), emptyList())
            val to =
                ComplexFraction<String>(emptyList(), emptyList())
            convert(from, to) shouldBe 1
        }

        "Из ничего в м/км возвращает 1000" {
            val from =
                ComplexFraction<String>(emptyList(), emptyList())
            val to = ComplexFraction(listOf("м"), listOf("км"))
            convert(from, to) shouldBe 1000
        }

        "Из км/м в ничего возвращает 1000" {
            val from = ComplexFraction(listOf("км"), listOf("м"))
            val to =
                ComplexFraction<String>(emptyList(), emptyList())
            convert(from, to) shouldBe 1000
        }

        "Сложный пример работает ((мин*мин*с*см)/(км*м) = K * (см*час*час*час)/(м*мм))" {
            val weights = mapOf(
                "мм" to 0.1,
                "см" to 1.0,
                "м" to 100.0,
                "км" to 100.0 * 1000.0,

                "с" to 1.0 / 60,
                "мин" to 1.0,
                "час" to 60.0
            )

            fun <T> List<T>.productOf(func: (T) -> Double) = fold(1.0) { acc, x -> acc * func(x) }
            fun List<String>.productOfWeights() = productOf { weights[it]!! }

            val fromX = listOf("мин", "мин", "с", "см")
            val fromY = listOf("км", "м")
            val toX = listOf("см", "час", "час", "час")
            val toY = listOf("м", "мм")

            val fromWeight = fromX.productOfWeights() / fromY.productOfWeights()
            val toWeight = toX.productOfWeights() / toY.productOfWeights()
            val expected = fromWeight / toWeight

            val from = ComplexFraction(fromX, fromY)
            val to = ComplexFraction(toX, toY)
            convert(from, to) shouldBe (expected plusOrMinus 1e-25)
        }

        "Метры в секунды бросает ConversionException" {
            val from = ComplexFraction(listOf("м"), emptyList())
            val to = ComplexFraction(listOf("с"), emptyList())
            shouldThrow<ConversionException> { convert(from, to) }
        }

        "Метры в луноходы и наоборот бросает NoSuchMeasurementException" {
            val from = ComplexFraction(listOf("м"), emptyList())
            val to = ComplexFraction(listOf("луноход"), emptyList())
            shouldThrow<NoSuchMeasurementException> { convert(from, to) }
            shouldThrow<NoSuchMeasurementException> { convert(to, from) }
        }

        "Секунда на метр в метр на секунду бросает ConversionException" {
            val from = ComplexFraction(listOf("с"), listOf("м"))
            val to = ComplexFraction(listOf("м"), listOf("с"))
            shouldThrow<ConversionException> { convert(from, to) }
        }

        "Различные длины в числителе и знаменателе бросает MismatchedDimensionalityException, обёрнутый в ConversionException" {
            val from = ComplexFraction(listOf("м"), listOf("с"))
            val to = ComplexFraction(listOf("м", "м"), listOf("с"))
            shouldThrow<ConversionException> { convert(from, to) }
                .shouldHaveCauseOfType<MismatchedDimensionalityException>()
        }
    }
}
