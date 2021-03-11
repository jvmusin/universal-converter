package jvmusin.universalconverter;

import jvmusin.universalconverter.exception.MeasurementConverterBuildException;

import java.nio.file.Path;
import java.util.List;

/**
 * Фабрика, используемая для создания {@link MeasurementConverter}.
 * <p>
 * Умеет создавать {@link MeasurementConverter} как из списка правил {@link ConversionRule},
 * так и из {@code .csv} файла.
 */
public interface MeasurementConverterFactory {
    /**
     * Создаёт {@link MeasurementConverter} из правил конвертации.
     * <p>
     * Для каждого правила кроме него самого в графе гарантированно будет и обратное правило.
     *
     * @param conversionRules правила конвертации
     * @param <TMeasurement>  тип единицы измерения
     * @return {@link MeasurementConverter}, умеющий приводить величины, указанные в правилах
     * @throws MeasurementConverterBuildException при наличии некорректных правил
     * @see ConversionRule#inverse()
     */
    <TMeasurement> MeasurementConverter<TMeasurement> create(List<ConversionRule<TMeasurement>> conversionRules);

    /**
     * Создаёт {@link MeasurementConverter} из правил конвертации, указанных в файле {@code csvFilePath}.
     * <p>
     * Для каждого правила кроме него самого в графе гарантированно будет и обратное правило.
     *
     * @param csvFilePath путь до {@code .csv} файла с правилами конвертации
     * @return {@link MeasurementConverter}, умеющий приводить величины, указанные в правилах из файла
     * @throws MeasurementConverterBuildException при наличии некорректных правил
     * @see ConversionRule#inverse()
     */
    MeasurementConverter<String> create(Path csvFilePath);
}
