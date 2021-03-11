package jvmusin.universalconverter;

import lombok.Data;

/**
 * Класс, описывающий соотношение одной единицы измерения к другой.
 * <p>
 * В одном {@code bigPiece} содержится {@code smallPieceCount} единиц {@code smallPiece}.
 *
 * @param <TMeasurement> тип величин измерения
 */
@Data
public class ConversionRule<TMeasurement> {
    private final TMeasurement bigPiece;
    private final TMeasurement smallPiece;
    private final double smallPieceCount;

    /**
     * Создаёт обратное правило (из 'меньшей' величины в 'большую' с коэффициентом {@code 1 / smallPieceCount}.
     *
     * @return обратное правило
     */
    public ConversionRule<TMeasurement> inverse() {
        return new ConversionRule<>(smallPiece, bigPiece, 1.0 / smallPieceCount);
    }
}
