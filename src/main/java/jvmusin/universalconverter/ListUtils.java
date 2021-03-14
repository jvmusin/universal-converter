package jvmusin.universalconverter;

import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

public class ListUtils {
    /**
     * Сливает два списка в один новый список, в котором сначала идут элементы первого списка,
     * затем элементы второго списка, в том же порядке, в котором они шли изначально.
     *
     * @param list1 первый список.
     * @param list2 второй список.
     * @param <T>   тип элементов списка.
     * @return Новый список, состоящий из элементов сначала первого, затем второго списка.
     * @throws IllegalArgumentException если хотя бы один из списков равен {@code null}.
     */
    public static <T> List<T> mergeLists(List<T> list1, List<T> list2) {
        Assert.notNull(list1, "Первый список равен null");
        Assert.notNull(list2, "Второй список равен null");
        List<T> res = new ArrayList<>(list1.size() + list2.size());
        res.addAll(list1);
        res.addAll(list2);
        return res;
    }
}
