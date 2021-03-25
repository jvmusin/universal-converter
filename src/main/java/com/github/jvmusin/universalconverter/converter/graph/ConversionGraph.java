package com.github.jvmusin.universalconverter.converter.graph;

import com.github.jvmusin.universalconverter.number.Number;
import java.util.Map;
import lombok.RequiredArgsConstructor;

/**
 * Граф конвертаций.
 *
 * <p>Для каждой величины измерения по её названию хранит {@link WeightedMeasurement}, который, в
 * свою очередь, содержит индекс сети конвертаций, которой принадлежит величина измерения, и её вес
 * в этой сети конвертаций.
 *
 * @param <TWeight> тип весов, используемых в графе.
 */
@RequiredArgsConstructor
public class ConversionGraph<TWeight extends Number<TWeight>> {
  private final Map<String, WeightedMeasurement<TWeight>> measurements;

  /**
   * Возвращает взвешенную величину измерения {@link WeightedMeasurement} с заданным названием
   * {@code name}.
   *
   * @param name название величины измерения.
   * @return Взвешенную величину измерения {@link WeightedMeasurement} с указанным названием или
   *     {@code null}, если такой величины измерения в этом графе нет.
   */
  public WeightedMeasurement<TWeight> getMeasurement(String name) {
    return measurements.get(name);
  }
}
