package jvmusin.universalconverter.web

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.checkAll
import io.kotest.property.exhaustive.exhaustive
import jvmusin.universalconverter.ComplexFraction
import jvmusin.universalconverter.web.WebUtils.convertExpressionToFraction

class ExpressionToFractionConversionTests : StringSpec({
    "Хорошие дроби парсятся верно" {
        convertExpressionToFraction("м / с") shouldBe ComplexFraction(listOf("м"), listOf("с"))
        convertExpressionToFraction("м / с") shouldBe ComplexFraction(listOf("м"), listOf("с"))
        convertExpressionToFraction("") shouldBe ComplexFraction(emptyList(), emptyList())
        convertExpressionToFraction("кг * м / с * с") shouldBe ComplexFraction(listOf("кг", "м"), listOf("с", "с"))
        convertExpressionToFraction("кг") shouldBe ComplexFraction(listOf("кг"), emptyList())
        convertExpressionToFraction("1") shouldBe ComplexFraction(emptyList(), emptyList())
        convertExpressionToFraction("1/1") shouldBe ComplexFraction(emptyList(), emptyList())
        convertExpressionToFraction("м/1") shouldBe ComplexFraction(listOf("м"), emptyList())
        convertExpressionToFraction("1/м") shouldBe ComplexFraction(emptyList(), listOf("м"))
        convertExpressionToFraction("1/м*с") shouldBe ComplexFraction(emptyList(), listOf("м", "с"))
    }

    "Плохие дроби не парсятся" {
        val tests = listOf(
            "/", "/с", "с/", "м//м", "м**м", "*", "*м", "м*", "*м*", "*м*м", "м*м/*м", "м*/м*м", "1/", "/1"
        )
        tests.exhaustive().checkAll { s ->
            shouldThrow<MalformedExpressionException> { convertExpressionToFraction(s) }
        }
    }
})
