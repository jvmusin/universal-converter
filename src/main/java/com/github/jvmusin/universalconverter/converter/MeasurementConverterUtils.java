package com.github.jvmusin.universalconverter.converter;

import static com.github.jvmusin.universalconverter.ListUtils.groupList;
import static com.github.jvmusin.universalconverter.ListUtils.mapList;
import static com.github.jvmusin.universalconverter.ListUtils.mergeLists;

import com.github.jvmusin.universalconverter.number.Number;
import java.util.List;
import java.util.Map;
import org.springframework.util.Assert;

public class MeasurementConverterUtils {
  /**
   * Строит по заданным правилам конвертации {@code rules} граф конвертации с весами типа {@link
   * TWeight}.
   *
   * <p>Граф конвертации имеет вид словаря, где ключом является единица измерения, а значением - все
   * рёбра, инцидентные этой единице измерения.
   *
   * <p>Например, если список правил содержит следующие правила вида {@code big->small smallCount}:
   *
   * <ul>
   *   <li>{@code a->b 2};
   *   <li>{@code b->c 4}.
   * </ul>
   *
   * То в графе конвертации будет выглядтель следующим образом:
   *
   * <ul>
   *   <li>{@code a}: [a->b 2]
   *   <li>{@code b}: [b->c 4, b->a 0.5]
   *   <li>{@code c}: [c->b 0.25]
   * </ul>
   *
   * @param rules правила конвертации.
   * @param <TWeight> тип весов в правилах конвертации и в графе.
   * @return Граф конвертации в виде словаря, где ключ - величина измерения, значение - все правила,
   *     напрямую относящиеся к этой величине измерения.
   * @throws IllegalArgumentException если список правил или хотя бы одно правило равно {@code
   *     null}.
   */
  public static <TWeight extends Number<TWeight>>
      Map<String, List<ConversionRule<TWeight>>> buildConversionGraph(
          List<ConversionRule<TWeight>> rules) {
    Assert.notNull(rules, "Список правил не может быть равен null");
    Assert.noNullElements(rules, "Список правил не может содержать null");
    List<ConversionRule<TWeight>> inverseRules = mapList(rules, ConversionRule::inverse);
    List<ConversionRule<TWeight>> allRules = mergeLists(rules, inverseRules);
    return groupList(allRules, ConversionRule::getBigPiece);
  }
}
