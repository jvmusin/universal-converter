package jvmusin.universalconverter

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.doubles.plusOrMinus
import io.kotest.matchers.shouldBe

class SuccessConverterTests : StringSpec({
    val converter = sampleConverter

    "Пример из условия работает" {
        val from = ComplexFraction(listOf("м"), listOf("с"))
        val to = ComplexFraction(listOf("км"), listOf("час"))
        converter.convert(from, to) shouldBe 3.6
    }

    "Обратный пример из условия работает" {
        val from = ComplexFraction(listOf("км"), listOf("час"))
        val to = ComplexFraction(listOf("м"), listOf("с"))
        converter.convert(from, to) shouldBe 1 / 3.6
    }

    "Километры в метры возвращает 1000" {
        val from = ComplexFraction(listOf("км"), emptyList())
        val to = ComplexFraction(listOf("м"), emptyList())
        converter.convert(from, to) shouldBe 1000
    }

    "Метры в километры возвращает 1/1000" {
        val from = ComplexFraction(listOf("м"), emptyList())
        val to = ComplexFraction(listOf("км"), emptyList())
        converter.convert(from, to) shouldBe 1.0 / 1000
    }

    "Секунда на метр в секунду на метр возвращает 1" {
        val from = ComplexFraction(listOf("с"), listOf("м"))
        val to = ComplexFraction(listOf("с"), listOf("м"))
        converter.convert(from, to) shouldBe 1
    }

    "Минута в квадрате в секунду в квадрате возвращает 60*60" {
        val from = ComplexFraction(listOf("мин", "мин"), emptyList())
        val to = ComplexFraction(listOf("с", "с"), emptyList())
        converter.convert(from, to) shouldBe 60 * 60
    }

    "Секунда в квадрате в минуту в квадрате возвращает 1/60/60" {
        val from = ComplexFraction(listOf("с", "с"), emptyList())
        val to = ComplexFraction(listOf("мин", "мин"), emptyList())
        converter.convert(from, to) shouldBe 1.0 / 60 / 60
    }

    "Неравные размерности по обеим сторонам работают" {
        val from = ComplexFraction(listOf("с", "мм", "м"), listOf("м", "мм"))
        val to = ComplexFraction(listOf("с", "м"), listOf("км"))
        converter.convert(from, to) shouldBe 1000
    }

    "Из ничего в ничего работает" {
        val from = ComplexFraction<String>(emptyList(), emptyList())
        val to = ComplexFraction<String>(emptyList(), emptyList())
        converter.convert(from, to) shouldBe 1
    }

    "Из ничего в м/км возвращает 1000" {
        val from = ComplexFraction<String>(emptyList(), emptyList())
        val to = ComplexFraction(listOf("м"), listOf("км"))
        converter.convert(from, to) shouldBe 1000
    }

    "Из км/м в ничего возвращает 1000" {
        val from = ComplexFraction(listOf("км"), listOf("м"))
        val to = ComplexFraction<String>(emptyList(), emptyList())
        converter.convert(from, to) shouldBe 1000
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
        converter.convert(from, to) shouldBe (expected plusOrMinus 1e-25)
    }
})
