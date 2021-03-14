package jvmusin.universalconverter

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import jvmusin.universalconverter.ListUtils.mergeLists

class ListUtilsTests : BehaviorSpec({
    Given("mergeLists") {
        When("первый список равен null") {
            Then("бросает IllegalArgumentException") {
                shouldThrow<IllegalArgumentException> {
                    mergeLists(null, listOf(1, 2, 3))
                }
            }
        }
        When("второй список равен null") {
            Then("бросает IllegalArgumentException") {
                shouldThrow<IllegalArgumentException> {
                    mergeLists(listOf(1, 2, 3), null)
                }
            }
        }
        When("оба списка равны null") {
            Then("бросает IllegalArgumentException") {
                shouldThrow<IllegalArgumentException> {
                    mergeLists<Int>(null, null)
                }
            }
        }
        When("оба списка не равны null") {
            Then("сливает как надо") {
                mergeLists(listOf(1, 2), listOf(3, 4, 5)) shouldBe listOf(1, 2, 3, 4, 5)
            }
        }
        When("в списках есть null значения") {
            Then("сливает как надо") {
                mergeLists(listOf(1, null, null, 2), listOf(3, null, 4, 5))
                    .shouldBe(listOf(1, null, null, 2, 3, null, 4, 5))
            }
        }
    }
})
