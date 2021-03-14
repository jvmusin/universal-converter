package jvmusin.universalconverter.converter.network

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.kotest.property.Arb
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.of
import io.kotest.property.checkAll
import jvmusin.universalconverter.converter.*
import jvmusin.universalconverter.converter.exception.NoSuchMeasurementException
import jvmusin.universalconverter.converter.network.exception.ConversionNetworkBuildException
import jvmusin.universalconverter.converter.network.exception.InfinitelyIncreasingCycleException
import jvmusin.universalconverter.converter.network.exception.NonPositiveWeightRuleException
import jvmusin.universalconverter.number.DoubleNumber
import jvmusin.universalconverter.number.DoubleNumberFactory

class ConversionNetworkTests : StringSpec({

    @Suppress("TestFunctionName")
    fun ConversionNetwork(conversionGraph: Map<String, List<ConversionRule<DoubleNumber>>>, root: String) =
        ConversionNetwork(
            conversionGraph,
            root,
            DoubleNumberFactory()
        )

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

    "Сеть бросает InfinitelyIncreasingCycleException при наличии бесконечно увеличивающихся циклов" {
        val wrongRule = sampleRules.first().let {
            ConversionRule(it.bigPiece, it.smallPiece, 32.0)
        }
        val rules = sampleRules + wrongRule
        shouldThrow<InfinitelyIncreasingCycleException> {
            ConversionNetwork(
                MeasurementConverterUtils.buildConversionGraph(rules),
                "м"
            )
        }
    }

    "Сеть бросает NonPositiveWeightRuleException при наличии нулевых рёбер" {
        val rules = (sampleRules + ConversionRule("м", "метро", 0.0))
        shouldThrow<NonPositiveWeightRuleException> { ConversionNetwork(rules.toConversionGraph(), "м") }
            .shouldBeInstanceOf<ConversionNetworkBuildException>()
    }

    "Сеть бросает NonPositiveWeightRuleException при наличии отрицательных рёбер" {
        val rules = (sampleRules + ConversionRule("м", "метро", -42.0))
        shouldThrow<NonPositiveWeightRuleException> { ConversionNetwork(rules.toConversionGraph(), "м") }
            .shouldBeInstanceOf<ConversionNetworkBuildException>()
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
