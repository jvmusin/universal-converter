package com.github.jvmusin.universalconverter.converter.graph;

import static com.github.jvmusin.universalconverter.ListUtils.groupList;
import static com.github.jvmusin.universalconverter.ListUtils.mapList;
import static com.github.jvmusin.universalconverter.ListUtils.mergeLists;

import com.github.jvmusin.universalconverter.converter.ConversionRule;
import com.github.jvmusin.universalconverter.number.Number;
import java.util.List;
import java.util.Map;
import org.springframework.util.Assert;

/**
 * Фабрика, используемая для создания графа конвертаций.
 *
 * @see #create(List)
 */
public class ConversionGraphFactory {

  /**
   * Строит по заданным правилам конвертации {@code rules} граф конвертации с весами типа {@link
   * TWeight}.
   *
   * <p>Граф конвертации имеет вид словаря, где ключом является величина измерения, а значением -
   * все рёбра, инцидентные этой величине измерения.
   *
   * <p>Например, если список правил содержит следующие правила вида {@code small->big smallCount}:
   *
   * <ul>
   *   <li>{@code мм->см 10};
   *   <li>{@code см->м 100}.
   * </ul>
   *
   * <p>То граф конвертации будет выглядеть следующим образом:
   *
   * <ul>
   *   <li>{@code мм}: [мм->см 10];
   *   <li>{@code см}: [см->мм 0.1, см->м 100];
   *   <li>{@code м}: [м->см 0.01].
   * </ul>
   *
   * <p>Таким образом, если пройти по пути от {@code мм} к {@code м} и перемножить все веса на
   * рёбрах (значения {@code smallCount}), то получится число {@code 1'000}, ровно во столько раз
   * один метр больше одного миллиметра.
   *
   * @param rules правила конвертации.
   * @param <TWeight> тип весов в правилах.
   * @return Граф конвертации в виде словаря, где ключ - величина измерения, значение - все правила,
   *     напрямую относящиеся к этой величине измерения.
   * @throws IllegalArgumentException если список правил или хотя бы одно правило равно {@code
   *     null}.
   */
  public <TWeight extends Number<TWeight>> Map<String, List<ConversionRule<TWeight>>> create(
      List<ConversionRule<TWeight>> rules) {
    Assert.notNull(rules, "Список правил не может быть равен null");
    Assert.noNullElements(rules, "Список правил не может содержать null");
    List<ConversionRule<TWeight>> inverseRules = mapList(rules, ConversionRule::inverse);
    List<ConversionRule<TWeight>> allRules = mergeLists(rules, inverseRules);
    return groupList(allRules, ConversionRule::getSmallPiece);
  }
}
