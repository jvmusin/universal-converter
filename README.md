# Universal Converter

Проект является решением тестового задания на Java-стажировку 2021 в Контур.  
Оригинал задания доступен в файле [TASK.md](TASK.md) и
в [оригинальном репозитории](https://github.com/gnkoshelev/universal-converter).

## Вкратце

Проект представляет собой *HTTP-сервис*, предоставляющий единственный *POST* метод `/convert`.  
Позволяет для выражений вида `1 м = 3.6 км * с / час` находить коэффициент дроби в правой части.  
Метод принимает на вход *JSON-сущность* в теле запроса с полями `from` и `to`, например:

```json
{
  "from": "м",
  "to": "км * с / час"
}
```

Пробелы в выражениях не важны, при поступлении запросов конвертер стирает все пробелы из тела
запроса.

На запросы конвертер отвечает единственным числом. Например, для примера выше, ответом будет `3.6`.

Для запуска сервиса необходимо в аргументах командной строки передать имя `.csv` файла, из которого
будут взяты правила конвертации. Тестовые правила конвертации доступны в [metrics.csv](metrics.csv).

## О реализации

### Граф конвертаций

При старте конвертер читает правила из `.csv` файла и по ним строит граф конвертаций, вершинами
которого являются величины измерения, а рёбрами - сами правила.  
На каждом ребре из `S` в `T` написано, сколько единиц `T` содержится в одном `S`.  
Для того, чтобы получить коэффициент перехода из величины `A` в величину `B`, необходимо перемножить
все веса на рёбрах на пути из `A` в `B`.

Например, если есть правила, где в одном `м` содержится **100** `см`, а в одном `см` содержится
**10** `мм`, тогда граф *может* выглядеть следующим образом:

| Величина |                         Рёбра                        |
|:--------:|:----------------------------------------------------:|
|    `м`   |              `м` &#8594; `см` : `1/100`              |
|   `см`   | `см` &#8594; `м` : `100`; `см` &#8594; `мм` : `1/10` |
|   `мм`   |               `мм` &#8594; `см` : `10`               |

В таком графе путь из `м` в `мм` имеет вес `1/1000` и означает, что в одном `м` содержится
тысяча `мм`.

### Сеть конвертаций

Граф конвертаций на самом деле состоит из множества сетей конвертаций, где каждая сеть отвечает за
свою систему счисления. То есть, граф конвертаций может содержать в себе как величины времени, так и
величины расстояния. Это величины не связаны между собой, а значит могут существовать отдельно друг
от друга, каждая в своей сети конвертаций.

Сеть конвертаций является связным графом. В каждой сети есть свой ***корневой элемент***. Корневой
элемент имеет вес **1**, а всем остальным элементам сети присвоен вес, пропорциональный весу
корневого элемента, который может быть вычислен как произведение коэффициентов на рёбрах на пути из
корневого элемента в конкретную величину измерения.

Таким образом, если в сети существуют величины `м`, `см` и `мм`, а корневым элементом является `см`,
то вес `см` равняется `1`, вес `м` равняется `100`, а вес `мм` равняется `1/10`.  
Обратите внимание, что эта сеть *эквивалентна* той, что указана в разделе про графы конвертаций.

После построения графа конвертаций, также строятся все сети конвертаций и для каждой величины
измерения запоминается, какой сети она принадлежит и каков её вес в этой сети.

Построение сети конвертаций происходит с помощью обхода в ширину. Такой подход позволяет дойти от
корневого элемента до всех остальных элементов, используя кратчайшие пути, а это, в свою очередь,
заметно уменьшает накапливаемую погрешность весов в случайных графах.

### Обработка запросов

Запросы приходят в виде двух дробей `числитель1 / знаменатель1` и `числитель2 / знаменатель2`.
Каждый числитель и знаменатель являются списками единиц измерения.

Коэффициентом, который необходимо найти, является `K` из
выражения `числитель1/знаменатель1 = K * числитель2/знаменатель2`.

Чтобы найти этот коэффициент, будем действовать следующим образом:

1. Преобразуем выражение: `числитель1*знаменатель2 / знаменатель1*числитель2 = K`;
2. Убедимся, что среди единиц измерения нет неизвестных величин;
    - Если это не так, то конвертация невозможна. Вернём код **404 Not Found**.
3. Склеим элементы числителя и знаменателя в новые списки: `числитель / знаменатель = K`;
    - Здесь `числитель = числитель1 + знаменатель2`, `знаменатель = знаменатель1 + числитель2`, где
      операция `+` - конкатенация списков.
4. Убедимся, что числитель и знаменатель имеют одинаковую длину;
    - Если это не так, то конвертация невозможна. Вернём код **400 Bad Request**.
5. Отсортируем элементы обоих списков по индексу сети, которым они принадлежат;
6. Убедимся, что для всех возможных индексов `i`, элементы `числитель[i]` и `знаменатель[i]`
   принадлежат одной сети;
    - Если это не так, то конвертация невозможна. Вернём код **400 Bad Request**.
7. Для всех возможных индексов `i` посчитаем соотношение весов числителя и
   знаменателя `вес[числитель[i]] / вес[знаменатель[i]]`;
8. Перемножим все коэффициенты, полученные на предыдущем шаге, это и будет ответом на запрос;
9. Вернём код **200 OK** и результат в теле ответа.

### Погрешности

При большом количестве операций с плавающей запятой, точность очень быстро теряется. Чтобы этого
избежать, можно использовать более точные типы данных.

#### Double

Самый простой, быстрый и, к сожалению, неточный тип данных.  
Быстро теряет точность и уже на небольших сетях будет накапливать большую погрешность.  
2 из 10, не рекомендую.  
Реализован в [DoubleNumber](src/main/java/jvmusin/universalconverter/number/DoubleNumber.java).

#### Дроби на BigInteger

Самый точный, но в то же время самый медленный тип данных. Дроби на `BigInteger` представляют собой
два числа — числитель и знаменатель, оба имеют тип `BigInteger`. Позволяют считать веса величин
измерения абсолютно без каких-либо погрешностей, но за это придётся заплатить большим потреблением
памяти и медленной работой конвертера в целом.  
7 из 10, использовать можно, если граф конвертаций не слишком большой.  
Реализован
в [BigIntFractionNumber](src/main/java/jvmusin/universalconverter/number/BigIntFractionNumber.java).

#### BigDecimal

Точнее, чем `Double`, быстрее, чем дроби, но погрешности остаются.  
Самый простой способ подсчёта с настраиваемой погрешностью.  
8 из 10, используется по умолчанию в конвертере.  
Реализован
в [BigDecimalNumber](src/main/java/jvmusin/universalconverter/number/BigDecimalNumber.java).

#### Другие способы избежать погрешностей

Также, чтобы избежать погрешностей, можно изменять порядок перемножения элементов при вычислении
ответа на запрос так, чтобы множители всегда были максимально близки к единице. Не понятно, как
оптимально переставить множители, чтобы добиться такого свойства, поэтому приём не используется в
конвертере.

## Заметки

### Валидация правил

Для того, чтобы не обманывать пользователей сервиса, при построении графа конвертаций есть смысл
проверять входной `.csv` файл на корректность. К некорректностям относятся:

- Наличие строк, которые невозможно прочитать из-за неверного формата строки;
    - Отлавливается.
- Наличие правил с нулевым или отрицательным весом;
    - Отлавливается.
- Наличие противоречащих правил, где в графе конвертаций между двумя величинами существуют пути
  разного веса.
    - Не отлавливается: Для этого нужно с очень высокой точностью сравнивать числа, но из-за
      накапливающейся погрешности сделать это возможно только на дробях, где погрешностей нет. На
      остальных типах данных возможны ложные срабатывания на корректных правилах.

## Технологический стек

Приложение:

- Java 11
- Lombok
- Spring Boot 2.4.3 (Web)
- Maven

Тестирование:

- Kotlin 1.4.31
- Kotest 4.4.3
- Spring Boot Test
- Jackson 2.12.2

Инфрастуктура:

- GitHub
- Maven Build GitHub Action
- Google Java Format GitHub Action
- GitHub Dependabot

Для управления зависимостями используется Maven.
