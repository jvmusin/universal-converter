package jvmusin.universalconverter.web;

import jvmusin.universalconverter.ComplexFraction;
import jvmusin.universalconverter.MeasurementConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контроллер, оперирующий запросами с телом запросов в формате {@code Json}.
 * Обрабатывает запросы на единственном пути {@code /convert}.
 */
@RestController
@Log4j2
@RequiredArgsConstructor
public class Controller {

    /**
     * Конвертер, используемый для подсчёта соотношения единиц измерения из запросов.
     */
    private final MeasurementConverter<String> converter;

    /**
     * Обрабатывает запросы на пути {@code /convert}. Конвертирует единицы измерения, предоставленные в теле запроса.
     * <p>
     * Если в теле запроса присутствуют неизвестные величины,
     * возвращает код {@code 404 Not Found} с пояснением в теле ответа.
     * <p>
     * Если запрос невозможно обработать по другим причинам,
     * возвращает код {@code 400 Bad Request} с пояснением в теле ответа.
     * <p>
     * Если запрос обработан успешно,
     * возвращает код {@code 200 OK} с коэффициентом соотношения
     * дроби {@code req.from} к {@code req.to} с точностью в 15 знаков в теле ответа.
     *
     * @param req тело запроса с единицами измерения, из какой единицы нужно конвертировать и в какую
     * @return коэффициент соотношения дроби {@code req.from} к {@code req.to} с точностью в 15 знаков
     */
    @PostMapping("/convert")
    public String convert(@RequestBody ConvertMeasurementValuesRequest req) {
        ComplexFraction<String> from = req.fromFraction();
        ComplexFraction<String> to = req.toFraction();
        double result = converter.convert(from, to);
        return String.format("%.15f", result);
    }
}
