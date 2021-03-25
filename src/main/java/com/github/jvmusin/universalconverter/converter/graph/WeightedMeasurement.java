package com.github.jvmusin.universalconverter.converter.graph;

import com.github.jvmusin.universalconverter.number.Number;
import lombok.Data;

/**
 * Величина измерения с присвоенными ей индексом сети конвертаций и весом в этой сети.
 *
 * @param <TWeight> тип веса величины измерения в сети конвертаций.
 */
@Data
public class WeightedMeasurement<TWeight extends Number<TWeight>> {

  /** Название величины измерения. */
  private final String name;

  /** Индекс сети конвертаций, которой принадлежит эта величина измерения. */
  private final int networkIndex;

  /** Вес этой величины измерения в её сети конвертаций. */
  private final TWeight weight;
}
