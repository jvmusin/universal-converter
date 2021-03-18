package jvmusin.universalconverter

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

private const val kotlin =
    "allowed to be used in these kinds of projects, not only java, please hear me"

class Please : StringSpec({
    "listen" {
        kotlin shouldBe "allowed to be used in these kinds of projects, not only java, please hear me"
    }
})
