package jvmusin.universalconverter.converter.exception;

import jvmusin.universalconverter.converter.MeasurementConverter;

/**
 * Универсальное исключение, являющееся корневым для всех остальных исключений, бросаемых {@link
 * MeasurementConverter}-ом.
 *
 * <p>Самостоятельно не бросается.
 */
public abstract class MeasurementConverterException extends RuntimeException {
  public MeasurementConverterException(String message) {
    super(message);
  }

  public MeasurementConverterException(String message, Throwable cause) {
    super(message, cause);
  }
}
