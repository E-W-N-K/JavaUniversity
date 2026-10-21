package by.sakun.fitness.model;

import java.time.LocalDate;
import java.util.Objects;

public final class Visit {
    private final long id;
    private final LocalDate date;

    public Visit(long id, LocalDate date) {
        if (id <= 0) throw new IllegalArgumentException("ID посещения должен быть положительным");
        this.id = id;
        this.date = Objects.requireNonNull(date, "Дата посещения обязательна");
    }

    public long getId() { return id; }
    public LocalDate getDate() { return date; }
}
