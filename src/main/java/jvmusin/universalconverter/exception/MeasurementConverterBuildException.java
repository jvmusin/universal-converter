package jvmusin.universalconverter.exception;

import jvmusin.universalconverter.MeasurementConverter;

/**
 * Исключение, выбрасываемое, если не удалось построить {@link MeasurementConverter}.
 */
public class MeasurementConverterBuildException extends MeasurementConverterException {
    public MeasurementConverterBuildException(String message, Throwable cause) {
        super(message, cause);
    }
}
