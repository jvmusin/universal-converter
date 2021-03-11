package jvmusin.universalconverter

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.of
import io.kotest.property.checkAll
import jvmusin.universalconverter.exception.ConversionNetworkBuildException

class ConversionNetworkTests : StringSpec({
    "Сеть строится при верных правилах" {
        ConversionNetwork(sampleConversionGraph, "м")
        ConversionNetwork(sampleConversionGraph, "с")
    }

    "Сеть строится при дублирующихся правилах".config(invocations = 10) {
        Arb.list(Arb.of(sampleRules), 1 until 10).checkAll { extraRules ->
            val rules = sampleRules + extraRules
            val conversionGraph = rules.toConversionGraph()
            ConversionNetwork(conversionGraph, "м")
            ConversionNetwork(conversionGraph, "с")
        }
    }

    "Сеть бросает ConversionNetworkBuildException при наличии неправильных циклов" {
        val wrongRule = sampleRules.first().let {
            ConversionRule(it.bigPiece, it.smallPiece, 32.0)
        }
        val rules = sampleRules + wrongRule
        shouldThrow<ConversionNetworkBuildException> { ConversionNetwork(Utils.buildConversionGraph(rules), "м") }
    }

    "Сеть отдаёт правильные веса" {
        val network = ConversionNetwork(sampleConversionGraph, "м")
        network.convertToCoefficient("м") shouldBe 1
        network.convertToCoefficient("см") shouldBe 1.0 / 100
        network.convertToCoefficient("мм") shouldBe 1.0 / 100 / 10
        network.convertToCoefficient("км") shouldBe 1000
    }

    "Сеть отдаёт правильные единицы измерения" {
        val network = ConversionNetwork(sampleConversionGraph, "м")
        network.measurements shouldBe setOf("м", "мм", "см", "км")
    }
})