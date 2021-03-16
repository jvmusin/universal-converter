package jvmusin.universalconverter.web

import com.fasterxml.jackson.module.kotlin.jsonMapper
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MockMvcResultMatchersDsl
import org.springframework.test.web.servlet.ResultActionsDsl
import org.springframework.test.web.servlet.post
import java.util.*

@Import(TestMeasurements::class)
@WebMvcTest(Controller::class)
class EndpointTests(private val mockMvc: MockMvc) : StringSpec() {

    private fun convert(
        from: String,
        to: String,
        expect: MockMvcResultMatchersDsl.() -> Unit
    ): ResultActionsDsl {
        return mockMvc.post("/convert") {
            contentType = MediaType.APPLICATION_JSON
            content = jsonMapper().writeValueAsString(ConvertMeasurementValuesRequest(from, to))
        }.andExpect(expect)
    }

    init {
        "mockMvc резоливится" {
            mockMvc.shouldNotBeNull()
        }

        "На м/c в км/час возвращает код 200 и результат 3.6" {
            convert("м / с", "км / час") {
                status { isOk() }
            }.andReturn().response.contentAsString.toDouble() shouldBe 3.6
        }

        "На приведение километров к метрам возвращается результат 1000" {
            convert("км", "м") {
                status { isOk() }
                content { "%.15f".format(Locale.ENGLISH, 1000.0) }
            }
        }

        "На неизвестную величину возвращается код 404" {
            convert("м / с", "км / пингвин") {
                status { isNotFound() }
            }
        }

        "На неприводимые величины возвращается код 400" {
            convert("м / с", "м / м") {
                status { isBadRequest() }
            }
        }

        "На неприводимые по длине дроби возвращается код 400" {
            convert("м / с", "м * м / с") {
                status { isBadRequest() }
            }
        }

        "На неприводимые по длине дроби и на неизвестные величины одновременно возвращает код 404" {
            convert("кг / с", "кг / год") {
                status { isNotFound() }
            }
        }
    }
}
