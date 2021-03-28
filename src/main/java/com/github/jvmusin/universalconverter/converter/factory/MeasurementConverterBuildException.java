package com.github.jvmusin.universalconverter.converter.factory;

import com.github.jvmusin.universalconverter.converter.MeasurementConverter;

/** Выбрасывается, если не удалось построить {@link MeasurementConverter}. */
public class MeasurementConverterBuildException extends RuntimeException {
  public MeasurementConverterBuildException(String message, Throwable cause) {
    super(message, cause);
  }
}
