package com.github.jvmusin.universalconverter;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import org.springframework.util.Assert;

public class ListUtils {
  /**
   * Сливает два списка в один новый список, в котором сначала идут элементы первого списка, затем
   * элементы второго списка, в том же порядке, в котором они шли изначально.
   *
   * @param list1 первый список.
   * @param list2 второй список.
   * @param <T> тип элементов списка.
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

  /**
   * Возвращает список, состоящий из элементов, полученных применением операции {@code mapper} ко
   * всем элементам списка {@code list}.
   *
   * @param list список, к элементам которого нужно применить операцию {@code mapper}.
   * @param mapper операция, которую нужно применить к элементам списка {@code list}.
   * @param <T> тип элементов исходного списка.
   * @param <R> тип элементов результирующего списка.
   * @return Новый список, где каждый элемент получен применением операции {@code mapper} к
   *     элементам списка {@code list}.
   */
  public static <T, R> List<R> mapList(List<T> list, Function<T, R> mapper) {
    Assert.notNull(list, "Список не может быть равен null");
    Assert.notNull(mapper, "Функция mapper не может быть равна null");
    return list.stream().map(mapper).collect(toList());
  }

  /**
   * Группирует элементы списка по ключу, полученному из функции {@code extractKey} в {@link Map
   * Map&lt;K, List&lt;T&gt;&gt;}, где ключ - это значение, полученное функцией {@code extractKey}
   * из элементов списка, а значение - список из всех элементов списка {@code list}, давших этот
   * ключ.
   *
   * @param list список, элементы которого нужно сгруппировать.
   * @param extractKey функция, извлекающая ключ из элементов списка.
   * @param <T> тип элементов списка.
   * @param <K> тип ключа.
   * @return Словарь {@link Map Map&lt;K, List&lt;T&gt;&gt;}, содержащий все элементы списка.
   * @throws IllegalArgumentException если список {@code list} или функция {@code extractKey} равны
   *     {@code null}.
   * @throws NullPointerException если {@code extractKey} возвращает {@code null}.
   */
  public static <T, K> Map<K, List<T>> groupList(List<T> list, Function<T, K> extractKey) {
    Assert.notNull(list, "Список не может быть равен null");
    Assert.notNull(extractKey, "Функция extractKey не может быть равна null");
    return list.stream().collect(groupingBy(extractKey));
  }
}
