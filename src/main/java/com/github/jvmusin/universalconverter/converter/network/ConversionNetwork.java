package com.github.jvmusin.universalconverter.converter.network;

import com.github.jvmusin.universalconverter.converter.exception.NoSuchMeasurementException;
import com.github.jvmusin.universalconverter.number.Number;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;

/**
 * Сеть величин измерения.
 *
 * <p>Позволяет переводить величины измерения от одной к другой в пределах одной и той же системы
 * счисления.
 *
 * <p>Назначает каждой величине измерения вес в зависимости от её относительной величины по
 * отношению к корневой величине измерения.
 *
 * <p>Например, если корневая величина измерения - метр, то у метра будет вес, равный величине, а у
 * километра вес, равный {@code 1000}. Если же корневой величиной измерения был километр, то его вес
 * будет величиной, а вес метра - {@code 1/1000}.
 *
 * @param <TWeight> тип весов, используемых сетью.
 */
@RequiredArgsConstructor
public class ConversionNetwork<TWeight extends Number<TWeight>> {
  /** Словарь, в котором для каждой величины измерения этой сети лежит соответствующий ей вес. */
  private final Map<String, TWeight> weights;

  /**
   * Возвращает все величины измерения в этой сети.
   *
   * @return Все величины измерения в этой сети.
   */
  public Set<String> getMeasurements() {
    return weights.keySet();
  }

  /**
   * Конвертирует величину измерения в присвоенный ей коэффициент.
   *
   * @param measurement величина измерения.
   * @return Коэффициент, присвоенный этой величине измерения.
   * @throws NoSuchMeasurementException если запрашиваемой величины измерения в этой сети нет.
   */
  public TWeight convertToCoefficient(String measurement) {
    TWeight result = weights.get(measurement);
    if (result == null)
      throw new NoSuchMeasurementException(
          "В этой сети нет нужной величины измерения: " + measurement);
    return result;
  }
}
