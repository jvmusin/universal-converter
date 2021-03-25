package com.github.jvmusin.universalconverter.converter.graph

import com.github.jvmusin.universalconverter.converter.*
import com.github.jvmusin.universalconverter.number.div
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.of
import io.kotest.property.checkAll

class ConversionGraphFactoryTests : StringSpec({

    "Граф строится при дублирующихся правилах" {
        Arb.list(Arb.of(sampleRules), 1 until 10).checkAll { extraRules ->
            val rules = sampleRules + extraRules
            val conversionGraph = rules.toConversionGraph()
            conversionGraph["км"]!!.weight / conversionGraph["м"]!!.weight shouldBe Weight(1000.0)
        }
    }

    "Бросает NonPositiveWeightRuleException при наличии нулевых рёбер" {
        val rules = (sampleRules + ConversionRule("м", "метро", 0.0))
        shouldThrow<NonPositiveWeightRuleException> { rules.toConversionGraph() }
    }

    "Бросает NonPositiveWeightRuleException при наличии отрицательных рёбер" {
        val rules = (sampleRules + ConversionRule("м", "метро", -42.0))
        shouldThrow<NonPositiveWeightRuleException> { rules.toConversionGraph() }
    }

    "Бросает IllegalArgumentException если список рёбер равен null" {
        shouldThrow<IllegalArgumentException> { conversionGraphFactory.create(null) }
    }

    "Бросает IllegalArgumentException если хотя бы одно правило равно null" {
        val rules = listOf(
            ConversionRule("a", "b", 2.0),
            null,
            ConversionRule("b", "c", 4.0)
        )
        shouldThrow<IllegalArgumentException> { conversionGraphFactory.create(rules) }
    }

    "Бросает NonPositiveWeightRuleException если существует правило с коэффициентом 0" {
        shouldThrow<NonPositiveWeightRuleException> {
            conversionGraphFactory.create(listOf(ConversionRule("a", "b", 0.0)))
        }
    }

    "Бросает NonPositiveWeightRuleException если существует правило с отрицательным коэффициентом" {
        shouldThrow<NonPositiveWeightRuleException> {
            conversionGraphFactory.create(listOf(ConversionRule("a", "b", -1.0)))
        }
    }
})
