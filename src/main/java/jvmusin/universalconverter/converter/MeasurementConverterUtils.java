package jvmusin.universalconverter.converter;

import jvmusin.universalconverter.ListUtils;
import jvmusin.universalconverter.number.Number;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

public class MeasurementConverterUtils {
    /**
     * Строит по заданным правилам конвертации {@code rules} граф конвертации с весами типа {@link TWeight}.
     * <p>
     * Граф конвертации имеет вид словаря, где ключом является единица измерения,
     * а значением - все рёбра, инцидентные этой единице измерения.
     * <p>
     * Например, если список правил содержит следующие правила вида {@code big->small smallCount}:
     * <ul>
     * <li>{@code a->b 2};</li>
     * <li>{@code b->c 4}.</li>
     * </ul>
     * То графе конвертации будет выглядтель следующим образом:
     * <ul>
     *     <li>{@code a}: [a->b 2]</li>
     *     <li>{@code b}: [b->c 4, b->a 0.5]</li>
     *     <li>{@code c}: [c->b 0.25]</li>
     * </ul>
     *
     * @param rules     правила конвертации.
     * @param <TWeight> тип весов в правилах конвертации и в графе.
     * @return Граф конвертации в виде словаря, где ключ - величина измерения,
     * значение - все правила, напрямую относящиеся к этой величине измерения.
     * @throws IllegalArgumentException если список правил или хотя бы одно правило равно {@code null}.
     */
    public static <TWeight extends Number<TWeight>> Map<String, List<ConversionRule<TWeight>>> buildConversionGraph(
            List<ConversionRule<TWeight>> rules) {
        Assert.notNull(rules, "Список правил не может быть равен null");
        Assert.noNullElements(rules, "Список правил не может содержать null");
        List<ConversionRule<TWeight>> inverseRules = rules.stream().map(ConversionRule::inverse).collect(toList());
        return ListUtils.mergeLists(rules, inverseRules)
                .stream()
                .collect(groupingBy(ConversionRule::getBigPiece));
    }
}
