package com.github.jvmusin.universalconverter.number

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class BigIntFractionNumberTests : BehaviorSpec({

    operator fun BigIntFractionNumber.times(other: BigIntFractionNumber) = multiplyBy(other)
    operator fun BigIntFractionNumber.div(other: BigIntFractionNumber) = divideBy(other)

    fun create(x: Int, y: Int = 1) = BigIntFractionNumber(x.toBigInteger(), y.toBigInteger())

    Given("создание") {

        fun createAndValidate(have: Pair<Int, Int>, need: Pair<Int, Int>) {
            val (haveNumerator, haveDenominator) = have
            val (needNominator, needDenominator) = need
            with(create(haveNumerator, haveDenominator)) {
                numerator.toInt() shouldBe needNominator
                denominator.toInt() shouldBe needDenominator
            }
        }

        When("нулевой знаменатель") {
            And("нулевой числитель") {
                Then("бросает ArithmeticException") {
                    shouldThrow<ArithmeticException> { create(0, 0) }
                }
            }
            And("ненулевой числитель") {
                Then("бросает ArithmeticException") {
                    shouldThrow<ArithmeticException> { create(123, 0) }
                }
            }
        }
        When("нулевой числитель") {
            Then("возвращает 0/1") {
                createAndValidate(0 to 123, 0 to 1)
            }
        }
        When("отрицательный знаменатель") {
            And("положительный числитель") {
                Then("меняют знаки") {
                    createAndValidate(3 to -2, -3 to 2)
                }
            }
            And("отрицательный числитель") {
                Then("меняют знаки") {
                    createAndValidate(-3 to -2, 3 to 2)
                }
            }
        }
        When("числитель и знаменатель не взаимно просты") {
            And("они положительны") {
                Then("результат делится на их НОД") {
                    createAndValidate(
                        3 * 5 * 7 to 5 * 11,
                        3 * 7 to 11
                    )
                }
            }
            And("числитель отрицателен") {
                Then("результат делится на их НОД") {
                    createAndValidate(
                        -3 * 5 * 7 to 5 * 11,
                        -3 * 7 to 11
                    )
                }
            }
            And("знаменатель отрицателен") {
                Then("результат делится на их НОД и меняются знаки") {
                    createAndValidate(
                        3 * 5 * 7 to -5 * 11,
                        -3 * 7 to 11
                    )
                }
            }
            And("они отрицательны") {
                Then("результат делится на их НОД") {
                    createAndValidate(
                        -3 * 5 * 7 to -5 * 11,
                        3 * 7 to 11
                    )
                }
            }
        }
    }

    Given("перемножение multiplyBy") {
        When("всё положительно") {
            Then("умножает как надо") {
                create(3, 5) * create(7, 11) shouldBe create(3 * 7, 5 * 11)
                create(3, 5) * create(7, 11 * 3) shouldBe create(7, 5 * 11)
            }
        }
        When("что-то отрицательно") {
            Then("умножает как надо") {
                create(-3, 5) * create(7, 11) shouldBe create(-3 * 7, 5 * 11)
            }
        }
        When("умножается на 0") {
            Then("получается 0") {
                create(-3, 5) * create(0) shouldBe create(0)
            }
        }
    }

    Given("деление divideBy") {
        When("всё положительно") {
            Then("делит как надо") {
                create(3, 5) / create(7, 11) shouldBe create(3 * 11, 5 * 7)
                create(3, 5) / create(7 * 3, 11) shouldBe create(11, 5 * 7)
            }
        }
        When("что-то отрицательно") {
            Then("делит как надо") {
                create(-3, 5) / create(7, 11) shouldBe create(-3 * 11, 5 * 7)
            }
        }
        When("умножается на 0") {
            Then("бросает ArithmeticException") {
                shouldThrow<ArithmeticException> { create(-3, 5) / create(0) }
            }
        }
    }

    Given("переворот дроби inverse") {
        When("дробь положительна") {
            Then("переворачивает как надо") {
                create(3, 5).inverse() shouldBe create(5, 3)
            }
        }
        When("дробь отрицательна") {
            Then("также меняет знаки") {
                create(-3, 5).inverse() shouldBe create(-5, 3)
            }
        }
        When("число 0") {
            Then("бросает ArithmeticException") {
                shouldThrow<ArithmeticException> { create(0).inverse() }
            }
        }
    }

    Given("toString") {
        When("1/3 * 1000") {
            Then("возвращает 34 значащих цифры и округляет вниз") {
                val x = create(1, 3) * create(1000)
                x.toString() shouldBe "333.3333333333333333333333333333333"
            }
        }
        When("2/3 * 1000") {
            Then("возвращает 34 значащих цифры и округляет вверх") {
                val x = create(2, 3) * create(1000)
                x.toString() shouldBe "666.6666666666666666666666666666667"
            }
        }
    }
})
