package jvmusin.universalconverter.converter.network.exception;

import jvmusin.universalconverter.converter.exception.MeasurementConverterException;
import jvmusin.universalconverter.converter.network.ConversionNetwork;

/** Выбрасывается, если не удалось построить {@link ConversionNetwork}. */
public abstract class ConversionNetworkBuildException extends MeasurementConverterException {
  public ConversionNetworkBuildException(String message) {
    super(message);
  }
}
