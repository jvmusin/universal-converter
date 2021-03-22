package com.github.jvmusin.universalconverter.web;

import com.github.jvmusin.universalconverter.converter.MeasurementConverter;
import com.github.jvmusin.universalconverter.fraction.ComplexFraction;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контроллер, оперирующий запросами с телом запросов в формате {@code Json}. Обрабатывает запросы
 * на единственном пути {@code /convert}.
 */
@RestController
@RequiredArgsConstructor
public class Controller {

  /** Конвертер, используемый для подсчёта соотношения величин измерения из запросов. */
  private final MeasurementConverter<?> converter;

  /**
   * Обрабатывает запросы на пути {@code /convert}. Конвертирует величины измерения, предоставленные
   * в теле запроса.
   *
   * <p>Если в теле запроса присутствуют неизвестные величины, возвращает код {@code 404 Not Found}
   * с пояснением в теле ответа.
   *
   * <p>Если запрос невозможно обработать по другим причинам, возвращает код {@code 400 Bad Request}
   * с пояснением в теле ответа.
   *
   * <p>Если запрос обработан успешно, возвращает код {@code 200 OK} с коэффициентом соотношения
   * дроби {@code req.from} к {@code req.to} с точностью в 15 значащих цифр в теле ответа.
   *
   * @param req тело запроса с величинами измерения, из какой величины нужно конвертировать и в
   *     какую.
   * @return Коэффициент соотношения дроби {@code req.from} к {@code req.to}.
   */
  @PostMapping("/convert")
  public String convert(@RequestBody ConvertMeasurementValuesRequest req) {
    ComplexFraction<String> from = req.fromFraction();
    ComplexFraction<String> to = req.toFraction();
    return converter.convert(from, to).toString();
  }
}
