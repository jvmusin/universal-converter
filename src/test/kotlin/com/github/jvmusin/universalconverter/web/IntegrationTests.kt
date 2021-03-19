package com.github.jvmusin.universalconverter.web

import io.kotest.core.spec.style.StringSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders

@SpringBootTest(webEnvironment = RANDOM_PORT, properties = ["csv=metrics.csv"])
@EnableAutoConfiguration
class IntegrationTests(@Autowired val restTemplate: TestRestTemplate) : StringSpec() {
    override fun extensions() = listOf(SpringExtension)

    private fun convert(from: String, to: String) = restTemplate.postForEntity<String>(
        "/convert",
        HttpEntity(
            """{"from": "$from", "to": "$to"}""",
            HttpHeaders().apply { this[HttpHeaders.CONTENT_TYPE] = "application/json" }
        )
    )

    init {
        "Приложение запускается" {}

        "На запрос из ничего в ничего возвращается 1" {
            val response = convert("", "")
            response.statusCodeValue shouldBe 200
            response.body shouldBe "1"
        }

        "На запрос из м в км*с/час возвращается (почти) 3.6" {
            val expected =
                "3.59999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999"
            val response = convert("м", "км*с/час")
            response.statusCodeValue shouldBe 200
            response.body shouldBe expected
        }

        "На приведение километров к метрам возвращается результат 1000" {
            val response = convert("км", "м")
            response.statusCodeValue shouldBe 200
            response.body shouldBe "1000"
        }

        "На неизвестную величину возвращается код 404" {
            val response = convert("м / с", "км / пингвин")
            response.statusCodeValue shouldBe 404
        }

        "На неприводимые величины возвращается код 400" {
            val response = convert("м / с", "м / м")
            response.statusCodeValue shouldBe 400
        }

        "На неприводимые по длине дроби возвращается код 400" {
            val response = convert("м / с", "м * м / с")
            response.statusCodeValue shouldBe 400
        }

        "На неприводимые по длине дроби и на неизвестные величины одновременно возвращается код 404" {
            val response = convert("кг / с", "кг / год")
            response.statusCodeValue shouldBe 404
        }
    }
}
