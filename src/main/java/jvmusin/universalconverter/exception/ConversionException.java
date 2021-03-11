package jvmusin.universalconverter.exception;

/**
 * Исключение, выбрасываемое, если произошла ошибка при конвертации единиц измерения.
 */
public class ConversionException extends MeasurementConverterException {
    public ConversionException(String message) {
        super(message);
    }

    public ConversionException(String message, Throwable cause) {
        super(message, cause);
    }
}
