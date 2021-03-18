package com.github.jvmusin.universalconverter.converter.exception;

import com.github.jvmusin.universalconverter.converter.MeasurementConverter;

/** Выбрасывается, если не удалось построить {@link MeasurementConverter}. */
public class MeasurementConverterBuildException extends MeasurementConverterException {
  public MeasurementConverterBuildException(String message, Throwable cause) {
    super(message, cause);
  }
}
