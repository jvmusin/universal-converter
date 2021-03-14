package jvmusin.universalconverter.converter;

import jvmusin.universalconverter.number.Number;
import lombok.Data;

/**
 * Класс, описывающий соотношение одной единицы измерения к другой.
 * <p>
 * В одном {@code bigPiece} содержится {@code smallPieceCount} единиц {@code smallPiece}.
 *
 * @param <TWeight> тип величины соотношения (веса).
 */
@Data
public class ConversionRule<TWeight extends Number<TWeight>> {
    private final String bigPiece;
    private final String smallPiece;
    private final TWeight smallPieceCount;

    /**
     * Создаёт обратное правило (из 'меньшей' величины в 'большую') с коэффициентом {@code 1 / smallPieceCount}.
     *
     * @return Обратное правило.
     */
    public ConversionRule<TWeight> inverse() {
        return new ConversionRule<>(smallPiece, bigPiece, smallPieceCount.inverse());
    }
}
