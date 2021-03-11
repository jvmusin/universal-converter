package jvmusin.universalconverter.exception;

import jvmusin.universalconverter.ConversionNetwork;

/**
 * Исключение, выбрасываемое, если не удалось построить {@link ConversionNetwork}.
 */
public class ConversionNetworkBuildException extends MeasurementConverterException {
    public ConversionNetworkBuildException(String message) {
        super(message);
    }
}
