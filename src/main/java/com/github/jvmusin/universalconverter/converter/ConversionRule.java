package com.github.jvmusin.universalconverter.converter;

import com.github.jvmusin.universalconverter.number.Number;
import lombok.Data;

/**
 * Класс, описывающий соотношение одной величины измерения к другой.
 *
 * <p>В одном {@code bigPiece} содержится {@code smallPieceCount} единиц {@code smallPiece}.
 *
 * @param <TWeight> тип величины соотношения (веса).
 */
@Data
public class ConversionRule<TWeight extends Number<TWeight>> {

  /** Название "большей" величины измерения. */
  private final String bigPiece;

  /** Название "меньшей" величины измерения. */
  private final String smallPiece;

  /** Количество единиц {@code smallPiece} в одном {@code bigPiece}. */
  private final TWeight smallPieceCount;

  /**
   * Создаёт обратное правило ("большая" становится "меньшей" и наоборот) с коэффициентом {@code
   * 1/smallPieceCount}.
   *
   * @return Обратное правило.
   */
  public ConversionRule<TWeight> inverse() {
    return new ConversionRule<>(smallPiece, bigPiece, smallPieceCount.inverse());
  }
}
