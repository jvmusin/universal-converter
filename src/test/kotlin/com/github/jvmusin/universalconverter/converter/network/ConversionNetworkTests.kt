package com.github.jvmusin.universalconverter.converter.network

import com.github.jvmusin.universalconverter.converter.ConversionRule
import com.github.jvmusin.universalconverter.converter.exception.NoSuchMeasurementException
import com.github.jvmusin.universalconverter.converter.sampleConversionGraph
import com.github.jvmusin.universalconverter.converter.sampleRules
import com.github.jvmusin.universalconverter.converter.toConversionGraph
import com.github.jvmusin.universalconverter.number.DoubleNumber
import com.github.jvmusin.universalconverter.number.DoubleNumberFactory
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.of
import io.kotest.property.checkAll

class ConversionNetworkTests : StringSpec({

    @Suppress("TestFunctionName")
    fun ConversionNetwork(
        conversionGraph: Map<String, List<ConversionRule<DoubleNumber>>>,
        root: String
    ): ConversionNetwork<DoubleNumber> {
        return ConversionNetworkFactory().create(conversionGraph, root, DoubleNumberFactory())
    }

    "Сеть строится при верных правилах" {
        ConversionNetwork(sampleConversionGraph, "м")
        ConversionNetwork(sampleConversionGraph, "с")
    }

    "Сеть строится при дублирующихся правилах" {
        Arb.list(Arb.of(sampleRules), 1 until 10).checkAll { extraRules ->
            val rules = sampleRules + extraRules
            val conversionGraph = rules.toConversionGraph()
            ConversionNetwork(conversionGraph, "м")
            ConversionNetwork(conversionGraph, "с")
        }
    }

    "Сеть бросает NonPositiveWeightRuleException при наличии нулевых рёбер" {
        val rules = (sampleRules + ConversionRule("м", "метро", 0.0))
        shouldThrow<NonPositiveWeightRuleException> {
            ConversionNetwork(rules.toConversionGraph(), "м")
        }
    }

    "Сеть бросает NonPositiveWeightRuleException при наличии отрицательных рёбер" {
        val rules = (sampleRules + ConversionRule("м", "метро", -42.0))
        shouldThrow<NonPositiveWeightRuleException> {
            ConversionNetwork(rules.toConversionGraph(), "м")
        }
    }

    "Сеть бросает NoSuchMeasurementException при запросе несуществующей величины на convertToCoefficient" {
        val network = ConversionNetwork(sampleConversionGraph, "м")
        shouldThrow<NoSuchMeasurementException> { network.convertToCoefficient("метро") }
    }

    "Сеть отдаёт правильные веса" {
        val network = ConversionNetwork(sampleConversionGraph, "м")
        network.convertToCoefficient("м").value shouldBe 1
        network.convertToCoefficient("см").value shouldBe 1.0 / 100
        network.convertToCoefficient("мм").value shouldBe 1.0 / 100 / 10
        network.convertToCoefficient("км").value shouldBe 1000
    }

    "Сеть отдаёт правильные единицы измерения" {
        val network = ConversionNetwork(sampleConversionGraph, "м")
        network.measurements shouldBe setOf("м", "мм", "см", "км")
    }
})
