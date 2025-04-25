package gov_service_api.controller;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/log")
@AllArgsConstructor
@Tag(name = "Логирование", description = "Эндпоинты для получения лог-файлов")
public class LogController {
    private static final DateTimeFormatter DATE_FORMATTER
            = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER
            = DateTimeFormatter.ofPattern("HH:mm");

    @Operation(summary = "Получить лог по определённой дате")
    @GetMapping("/{date}")
    public ResponseEntity<List<String>> getLogsByDate(@PathVariable String date,
                                                      @RequestParam(required = false) String from,
                                                      @RequestParam(required = false) String to) {
        try {
            LocalDate logDate = LocalDate.parse(date, DATE_FORMATTER);

            String filename = String.format("logs/app-%s.log", DATE_FORMATTER.format(logDate));
            Path filePath = Paths.get(filename);

            if (!Files.exists(filePath)) {
                throw new ResponseStatusException(NOT_FOUND,
                        "Лог-файл за дату " + date + " не найден");
            }

            LocalTime fromTime = from != null ? LocalTime.parse(
                    from, TIME_FORMATTER) : LocalTime.MIN;
            LocalTime toTime = to != null ? LocalTime.parse(
                    to, TIME_FORMATTER) : LocalTime.MAX;

            try (Stream<String> lines = Files.lines(filePath)) {
                List<String> filteredLines = lines
                        .filter(line -> {
                            try {
                                String timePart = line.substring(11, 16);
                                LocalTime logTime = LocalTime.parse(timePart, TIME_FORMATTER);
                                return !logTime.isBefore(fromTime) && !logTime.isAfter(toTime);
                            } catch (Exception e) {
                                return false;
                            }
                        })
                        .toList();

                return ResponseEntity.ok(filteredLines);
            }
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(BAD_REQUEST,
                    "Неверный формат запроса: " + e.getMessage());
        }
    }
}
