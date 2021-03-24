package com.github.jvmusin.universalconverter.converter.network;

import com.github.jvmusin.universalconverter.converter.exception.MeasurementConverterException;

/** Выбрасывается, если в сети нашлось правило с неположительным весом. */
public class NonPositiveWeightRuleException extends MeasurementConverterException {
  public NonPositiveWeightRuleException(String message) {
    super(message);
  }
}
