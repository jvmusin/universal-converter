package com.github.jvmusin.universalconverter.converter;

import com.github.jvmusin.universalconverter.converter.exception.ConversionException;
import com.github.jvmusin.universalconverter.converter.exception.NoSuchMeasurementException;
import com.github.jvmusin.universalconverter.fraction.ComplexFraction;
import com.github.jvmusin.universalconverter.number.Number;

/**
 * Конвертер, позволяющий получать коэффициент соотношения одной величины измерения к другой.
 *
 * @param <TWeight> тип промежуточных операций и результата.
 */
public interface MeasurementConverter<TWeight extends Number<TWeight>> {
  /**
   * Находит коэффициент соотношения величины измерения {@code from} к величине измерения {@code
   * to}.
   *
   * <p>Если представить дробь {@code from} в виде {@code a/b}, а дробь {@code to} в виде {@code
   * c/d}, то эта функция найдёт коэффициент {@code K} такой, что {@code a/b = K * c/d}.
   *
   * @param from величина измерения, из которой производится перевод.
   * @param to величина измерения, в которую производится перевод.
   * @return Коэффициент соотношения величины измерения {@code from} к величине измерения {@code
   *     to}.
   * @throws NoSuchMeasurementException если в дробях присутствует неизвестная величина измерения.
   * @throws ConversionException если конвертация невозможна по иным причинам.
   */
  TWeight convert(ComplexFraction<String> from, ComplexFraction<String> to);
}
