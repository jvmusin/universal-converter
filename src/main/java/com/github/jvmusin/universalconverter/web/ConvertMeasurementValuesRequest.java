package com.github.jvmusin.universalconverter.web;

import com.github.jvmusin.universalconverter.fraction.ComplexFraction;
import lombok.Data;

/**
 * Класс, описывающий запрос на конвертацию величины измерения из {@code from} в {@code to}.
 *
 * <p>Используется как сущность, отправляемая в теле запроса на {@code HTTP} метод {@code /convert}.
 */
@Data
public class ConvertMeasurementValuesRequest {
  /** Величина измерения, из которой нужно произвести конвертацию. */
  private final String from;

  /** Величина измерения, в которую нужно произвести конвертацию. */
  private final String to;

  /**
   * Конвертирует выражение из {@code from} в дробь типа {@link ComplexFraction}.
   *
   * @return Дробь, соответствующую значению {@code from}.
   * @throws MalformedExpressionException если выражение не удалось конвертировать в дробь.
   */
  public ComplexFraction<String> fromFraction() {
    return WebUtils.convertExpressionToFraction(from);
  }

  /**
   * Конвертирует выражение из {@code to} в дробь типа {@link ComplexFraction}.
   *
   * @return Дробь, соответствующую значению {@code to}.
   * @throws MalformedExpressionException если выражение не удалось конвертировать в дробь.
   */
  public ComplexFraction<String> toFraction() {
    return WebUtils.convertExpressionToFraction(to);
  }
}
