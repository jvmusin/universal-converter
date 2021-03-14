package jvmusin.universalconverter.converter.network.exception;

/**
 * Выбрасывается, если в сети нашёлся бесконечно увеличивающийся цикл.
 */
public class InfinitelyIncreasingCycleException extends ConversionNetworkBuildException {
    public InfinitelyIncreasingCycleException(String message) {
        super(message);
    }
}
