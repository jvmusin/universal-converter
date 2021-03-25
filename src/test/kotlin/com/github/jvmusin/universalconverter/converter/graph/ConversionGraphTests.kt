package com.github.jvmusin.universalconverter.converter.graph

import com.github.jvmusin.universalconverter.converter.Weight
import com.github.jvmusin.universalconverter.converter.get
import com.github.jvmusin.universalconverter.converter.sampleConversionGraph
import com.github.jvmusin.universalconverter.number.DoubleNumber
import com.github.jvmusin.universalconverter.number.div
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe

class ConversionGraphTests : StringSpec({
    "Граф возвращает null при запросе несуществующей величины" {
        sampleConversionGraph["метро"].shouldBeNull()
    }

    "Граф отдаёт правильные веса" {
        fun convert(from: String, to: String): DoubleNumber {
            return sampleConversionGraph[from]!!.weight / sampleConversionGraph[to]!!.weight
        }
        convert("м", "см") shouldBe Weight(100.0)
        convert("км", "м") shouldBe Weight(1000.0)
        convert("мин", "с") shouldBe Weight(60.0)
        convert("час", "мин") shouldBe Weight(60.0)
    }
})
