package jvmusin.universalconverter.exception;

/**
 * Исключение, выбрасываемое, если величина измерения не найдена.
 */
public class NoSuchMeasurementException extends ConversionException {
    public NoSuchMeasurementException(String message) {
        super(message);
    }
}
