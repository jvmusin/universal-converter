package jvmusin.universalconverter;

import jvmusin.universalconverter.exception.MeasurementConverterBuildException;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static jvmusin.universalconverter.Utils.buildConversionGraph;

@Log4j2
@Service
public class MeasurementConverterFactoryImpl implements MeasurementConverterFactory {

    private static final String MORE_THAN_THREE_TOKENS_MESSAGE = "Строка состоит не из трёх токенов";
    private static final String NO_EMPTY_TOKENS_MESSAGE = "В строке не должно быть пустых токенов";
    private static final String ONLY_POSITIVE_COEFFICIENT_MESSAGE = "Коэффициенты должны быть строго положительны";

    @Override
    public <TMeasurement> MeasurementConverter<TMeasurement> create(List<ConversionRule<TMeasurement>> conversionRules) {
        try {
            Map<TMeasurement, List<ConversionRule<TMeasurement>>> conversionGraph = buildConversionGraph(conversionRules);
            Set<TMeasurement> usedMeasurements = new HashSet<>();
            Map<TMeasurement, Integer> measurementToNetwork = new HashMap<>();
            List<ConversionNetwork<TMeasurement>> networks = new ArrayList<>();
            for (Map.Entry<TMeasurement, List<ConversionRule<TMeasurement>>> e : conversionGraph.entrySet()) {
                TMeasurement measurement = e.getKey();
                if (!usedMeasurements.contains(measurement)) {
                    ConversionNetwork<TMeasurement> network = new ConversionNetwork<>(conversionGraph, measurement);
                    int networkIndex = networks.size();
                    networks.add(network);
                    Set<TMeasurement> networkMeasurements = network.getMeasurements();
                    usedMeasurements.addAll(networkMeasurements);
                    for (TMeasurement m : networkMeasurements) measurementToNetwork.put(m, networkIndex);
                }
            }
            return new MeasurementConverterImpl<>(networks, measurementToNetwork);
        } catch (Exception e) {
            throw new MeasurementConverterBuildException("Не удалось построить MeasurementConverter", e);
        }
    }

    @Override
    @SneakyThrows
    public MeasurementConverter<String> create(Path csvFilePath) {
        log.info("Читаем правила конвертации из файла " + csvFilePath);
        try (Stream<String> lines = Files.lines(csvFilePath)) {
            List<ConversionRule<String>> rules = lines
                    .peek(log::debug)
                    .map(s -> s.split(","))
                    .peek(x -> Assert.isTrue(x.length == 3, MORE_THAN_THREE_TOKENS_MESSAGE))
                    .peek(x -> Assert.isTrue(Arrays.stream(x).noneMatch(String::isEmpty), NO_EMPTY_TOKENS_MESSAGE))
                    .map(x -> new ConversionRule<>(x[0], x[1], Double.parseDouble(x[2])))
                    .peek(r -> Assert.isTrue(r.getSmallPieceCount() > 0, ONLY_POSITIVE_COEFFICIENT_MESSAGE))
                    .collect(toList());
            return create(rules);
        } catch (Exception e) {
            throw new MeasurementConverterBuildException(
                    "Не удалось построить MeasurementConverter из файла " + csvFilePath, e
            );
        }
    }
}
