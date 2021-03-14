package jvmusin.universalconverter.converter.factory;

import jvmusin.universalconverter.converter.ConversionRule;
import jvmusin.universalconverter.converter.MeasurementConverter;
import jvmusin.universalconverter.converter.exception.MeasurementConverterBuildException;
import jvmusin.universalconverter.number.Number;
import jvmusin.universalconverter.number.NumberFactory;
import lombok.extern.log4j.Log4j2;
import org.springframework.util.Assert;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * Фабрика, используемая для создания {@link MeasurementConverter} из правил в {@code .csv} файле.
 * <p>
 * Создаваемые этой фабрикой конвертеры оперируют весами типа {@link TWeight}.
 *
 * @param <TWeight> тип весов, используемых создаваемыми конвертерами.
 */
@Log4j2
public class CsvFileSourcedMeasurementConverterFactory<TWeight extends Number<TWeight>>
        extends MeasurementConverterFactoryImpl<TWeight> {

    public static final String MORE_THAN_THREE_TOKENS_MESSAGE = "Строка состоит не из трёх токенов";
    public static final String EMPTY_TOKEN_MESSAGE = "В строке не должно быть пустых токенов";

    public CsvFileSourcedMeasurementConverterFactory(NumberFactory<TWeight> weightFactory) {
        super(weightFactory);
    }

    /**
     * Создаёт {@link MeasurementConverter} на весах типа {@link TWeight}
     * из правил конвертации из файла {@code csvFilePath}.
     * <p>
     * Все пустые строки файла пропускаются.
     * В файле строки должны иметь вид <pre>big,small,coefficient</pre>
     * где {@code big} - большая величина, {@code small} - меньшая величина,
     * {@code coefficient} - сколько меньших величин в одной большей,
     * например <pre>км,м,1000</pre> или <pre>мм,м,0.001</pre>
     *
     * @param csvFilePath путь до файла с правилами конвертации.
     * @return {@link MeasurementConverter}, работающий по правилам, указанным в файле с правилами конвертации.
     * @throws MeasurementConverterBuildException при ошибке построения конвертера.
     */
    public MeasurementConverter<TWeight> create(Path csvFilePath) {
        try (Stream<String> lines = Files.lines(csvFilePath)) {
            log.info("Читаем правила конвертации из файла " + csvFilePath);
            List<ConversionRule<TWeight>> rules = lines
                    .peek(log::debug)
                    .filter(line -> !line.isBlank())
                    .map(line -> line.split(","))
                    .peek(row -> Assert.isTrue(row.length == 3, MORE_THAN_THREE_TOKENS_MESSAGE))
                    .peek(row -> Assert.isTrue(Arrays.stream(row).noneMatch(String::isEmpty), EMPTY_TOKEN_MESSAGE))
                    .map(row -> new ConversionRule<>(row[0], row[1], weightFactory.parse(row[2])))
                    .collect(toList());
            return create(rules);
        } catch (Exception e) {
            throw new MeasurementConverterBuildException(
                    "Не удалось построить MeasurementConverter из файла " + csvFilePath, e
            );
        }
    }
}
