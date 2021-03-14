package jvmusin.universalconverter.converter.network.exception;

import jvmusin.universalconverter.converter.network.ConversionNetwork;
import jvmusin.universalconverter.converter.exception.MeasurementConverterException;

/**
 * Выбрасывается, если не удалось построить {@link ConversionNetwork}.
 */
public abstract class ConversionNetworkBuildException extends MeasurementConverterException {
    public ConversionNetworkBuildException(String message) {
        super(message);
    }
}
