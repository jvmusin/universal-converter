package jvmusin.universalconverter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

public class Utils {
    /**
     * Сливает два списка в один новый список, в котором сначала идут элементы первого списка,
     * затем элементы второго списка, в том же порядке, в котором они были изначально.
     *
     * @param list1 первый список
     * @param list2 второй список
     * @param <T>   тип элементов списка
     * @return новый список, состоящий из элементов сначала первого, затем второго списка
     */
    public static <T> List<T> mergeLists(List<T> list1, List<T> list2) {
        List<T> res = new ArrayList<>(list1.size() + list2.size());
        res.addAll(list1);
        res.addAll(list2);
        return res;
    }

    /**
     * Строит по заданным правилам конвертации граф конвертации.
     *
     * @param conversionRules правила конвертации
     * @param <TMeasurement>  тип единицы измерения
     * @return граф конвертации в виде словаря, где ключ - величина измерения,
     * значение - все правила, напрямую относящиеся к этой единице измерения
     */
    public static <TMeasurement> Map<TMeasurement, List<ConversionRule<TMeasurement>>> buildConversionGraph(
            List<ConversionRule<TMeasurement>> conversionRules) {
        List<ConversionRule<TMeasurement>> inverseRules = conversionRules.stream()
                .map(ConversionRule::inverse).collect(toList());
        return mergeLists(conversionRules, inverseRules).stream().collect(groupingBy(ConversionRule::getBigPiece));
    }
}
