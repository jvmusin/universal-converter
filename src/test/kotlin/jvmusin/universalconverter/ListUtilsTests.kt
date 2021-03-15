package jvmusin.universalconverter

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.maps.shouldBeEmpty
import io.kotest.matchers.maps.shouldContainExactly
import io.kotest.matchers.shouldBe
import jvmusin.universalconverter.ListUtils.*

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

    Given("mapList") {
        When("список равен null") {
            Then("бросает IllegalArgumentException") {
                shouldThrow<IllegalArgumentException> { mapList<Int, Int>(null) { it } }
            }
        }
        When("mapper равен null") {
            Then("бросает IllegalArgumentException") {
                shouldThrow<IllegalArgumentException> { mapList<Int, Int>(listOf(1, 2, 3), null) }
            }
        }
        When("список пуст") {
            Then("возвращает пустой список") {
                mapList(listOf<Int>()) { it }.shouldBeEmpty()
            }
        }
        When("список состоит из 1 элемента") {
            Then("возвращает 1 элемент") {
                mapList(listOf(1)) { it * 2 } shouldBe listOf(2)
            }
        }
        When("список состоит из нескольких элементов") {
            Then("возвращает все элементы") {
                mapList(listOf(1, 2, 3, 5, 2)) { it * 2 } shouldBe listOf(2, 4, 6, 10, 4)
            }
        }
        When("на некоторых элементах списка возвращается null") {
            Then("возвращает список с null значениями") {
                mapList(listOf(1, 2, 3, 4, 5)) { if (it % 2 == 0) null else it } shouldBe listOf(1, null, 3, null, 5)
            }
        }
        When("некоторые элементы списка равны null") {
            And("mapper их правильно обрабатывает") {
                Then("возвращает правильный список") {
                    mapList(listOf(1, null, 3)) { it ?: -1 } shouldBe listOf(1, -1, 3)
                }
            }
        }
    }

    Given("groupList") {
        When("список равен null") {
            Then("бросает IllegalArgumentException") {
                shouldThrow<IllegalArgumentException> { groupList<Int, Int>(null) { it } }
            }
        }
        When("mapper равен null") {
            Then("бросает IllegalArgumentException") {
                shouldThrow<IllegalArgumentException> { groupList<Int, Int>(listOf(1, 2, 3), null) }
            }
        }
        When("mapper возвращает null") {
            Then("бросает NullPointerException") {
                shouldThrow<NullPointerException> { groupList(listOf(1, 2, 3)) { if (it % 2 == 1) it else null } }
            }
        }
        When("список пуст") {
            Then("возвращает пустой словарь") {
                groupList(listOf<Int>()) { it }.shouldBeEmpty()
            }
        }
        When("список состоит из 1 элемента") {
            Then("возвращает словарь из 1 элемента") {
                groupList(listOf(5)) { it * 2 } shouldBe mapOf(10 to listOf(5))
            }
        }
        When("все элементы списка дают разные ключи") {
            Then("возвращает словарь со списками по одному элементу") {
                val list = listOf(1, 2, 3)
                val expected = mapOf(
                    2 to listOf(1),
                    4 to listOf(2),
                    6 to listOf(3)
                )
                groupList(list) { it * 2 }.shouldContainExactly(expected)
            }
        }
        When("некоторые элементы списка дают одинаковые ключи") {
            Then("группирует элементы с одинаковыми значениями в один список") {
                val list = listOf(1, 2, 3, 4, 5)
                val expected = mapOf(
                    0 to listOf(3),
                    1 to listOf(1, 4),
                    2 to listOf(2, 5)
                )
                groupList(list) { it % 3 }.shouldContainExactly(expected)
            }
        }
        When("некоторые элементы списка равны null") {
            And("extractKey обрабатывает их нормально") {
                Then("возвращает правильный словарь") {
                    val list = listOf(1, 2, null, 3)
                    val expected = mapOf(
                        0 to listOf(2),
                        1 to listOf(1, 3),
                        -1 to listOf(null)
                    )
                    groupList(list) { if (it != null) it % 2 else -1 }.shouldContainExactly(expected)
                }
            }
        }
    }
})
