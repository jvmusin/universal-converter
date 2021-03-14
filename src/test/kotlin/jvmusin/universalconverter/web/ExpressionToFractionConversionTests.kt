package jvmusin.universalconverter.web

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import jvmusin.universalconverter.fraction.ComplexFraction
import jvmusin.universalconverter.web.WebUtils.convertExpressionToFraction

class ExpressionToFractionConversionTests : StringSpec({
    "Хорошие дроби парсятся верно" {
        convertExpressionToFraction("") shouldBe ComplexFraction(emptyList(), emptyList())
        convertExpressionToFraction("м / с") shouldBe ComplexFraction(listOf("м"), listOf("с"))
        convertExpressionToFraction("кг * м / с * с") shouldBe ComplexFraction(listOf("кг", "м"), listOf("с", "с"))
        convertExpressionToFraction("кг") shouldBe ComplexFraction(listOf("кг"), emptyList())
        convertExpressionToFraction("1") shouldBe ComplexFraction(emptyList(), emptyList())
        convertExpressionToFraction("1/1") shouldBe ComplexFraction(emptyList(), emptyList())
        convertExpressionToFraction("м/1") shouldBe ComplexFraction(listOf("м"), emptyList())
        convertExpressionToFraction("1/м") shouldBe ComplexFraction(emptyList(), listOf("м"))
        convertExpressionToFraction("1/м*с") shouldBe ComplexFraction(emptyList(), listOf("м", "с"))
    }

    "На null бросается IllegalArgumentException" {
        shouldThrow<IllegalArgumentException> { convertExpressionToFraction(null) }
    }

    "Плохие дроби не парсятся" {
        val badRationals = listOf(
            "/", "/с", "с/", "м//м", "м**м", "*", "*м", "м*", "*м*", "*м*м", "м*м/*м", "м*/м*м", "1/", "/1"
        )
        for (rational in badRationals) {
            shouldThrow<MalformedExpressionException> {
                convertExpressionToFraction(rational)
            }
        }
    }
})
