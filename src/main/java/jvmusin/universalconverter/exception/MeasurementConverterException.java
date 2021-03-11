package jvmusin.universalconverter.exception;

import jvmusin.universalconverter.MeasurementConverter;

/**
 * Универсальное исключение, являющееся корневым для всех остальных исключений,
 * бросаемых {@link MeasurementConverter}-ом.
 * <p>
 * Самостоятельно не бросается
 */
public class MeasurementConverterException extends RuntimeException {
    public MeasurementConverterException(String message) {
        super(message);
    }

    public MeasurementConverterException(String message, Throwable cause) {
        super(message, cause);
    }
}
