package jvmusin.universalconverter.converter.exception;

import jvmusin.universalconverter.converter.MeasurementConverter;

/** Выбрасывается, если не удалось построить {@link MeasurementConverter}. */
public class MeasurementConverterBuildException extends MeasurementConverterException {
  public MeasurementConverterBuildException(String message, Throwable cause) {
    super(message, cause);
  }
}
