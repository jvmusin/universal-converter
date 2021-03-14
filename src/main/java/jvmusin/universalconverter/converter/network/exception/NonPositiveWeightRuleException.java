package jvmusin.universalconverter.converter.network.exception;

/**
 * Выбрасывается, если в сети нашлось правило с неположительным весом.
 */
public class NonPositiveWeightRuleException extends ConversionNetworkBuildException {
    public NonPositiveWeightRuleException(String message) {
        super(message);
    }
}
