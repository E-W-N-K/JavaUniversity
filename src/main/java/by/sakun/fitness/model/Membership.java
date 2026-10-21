package by.sakun.fitness.model;

import by.sakun.fitness.policy.VisitPolicy;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Membership {
    private final long id;
    private final Client client;
    private final int durationMonths;
    private final VisitPolicy policy;
    private final List<Visit> visits = new ArrayList<>();
    private MembershipStatus status = MembershipStatus.CREATED;
    private LocalDate startDate;
    private LocalDate endDate;

    public Membership(long id, Client client, int durationMonths, VisitPolicy policy) {
        if (id <= 0) throw new IllegalArgumentException("ID абонемента должен быть положительным");
        if (durationMonths <= 0 || durationMonths > 120) throw new IllegalArgumentException("Срок должен быть от 1 до 120 месяцев");
        this.id = id;
        this.client = Objects.requireNonNull(client, "Клиент обязателен");
        this.policy = Objects.requireNonNull(policy, "Правило посещений обязательно");
        this.durationMonths = durationMonths;
    }

    public void activate(LocalDate date) {
        Objects.requireNonNull(date, "Дата активации обязательна");
        if (status != MembershipStatus.CREATED) throw new IllegalStateException("Абонемент уже активирован");
        startDate = date;
        endDate = date.plusMonths(durationMonths).minusDays(1);
        status = MembershipStatus.ACTIVE;
    }

    public Visit recordVisit(long visitId, LocalDate date) {
        Objects.requireNonNull(date, "Дата посещения обязательна");
        if (status != MembershipStatus.ACTIVE) throw new IllegalStateException("Абонемент не активен");
        if (date.isBefore(startDate) || date.isAfter(endDate)) {
            if (date.isAfter(endDate)) status = MembershipStatus.EXPIRED;
            throw new IllegalStateException("Дата вне срока действия абонемента");
        }
        if (!policy.canVisit(this, date)) throw new IllegalStateException("Лимит посещений исчерпан");
        Visit visit = new Visit(visitId, date);
        visits.add(visit);
        return visit;
    }

    public long getId() { return id; }
    public Client getClient() { return client; }
    public MembershipStatus getStatus() { return status; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public String getPlanName() { return policy.name(); }
    public List<Visit> getVisits() { return List.copyOf(visits); }
}
