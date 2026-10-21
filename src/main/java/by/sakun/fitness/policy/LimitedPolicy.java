package by.sakun.fitness.policy;

import by.sakun.fitness.model.Membership;
import java.time.LocalDate;

public final class LimitedPolicy implements VisitPolicy {
    private final int monthlyLimit;

    public LimitedPolicy(int monthlyLimit) {
        if (monthlyLimit <= 0) throw new IllegalArgumentException("Лимит должен быть положительным");
        this.monthlyLimit = monthlyLimit;
    }

    public boolean canVisit(Membership membership, LocalDate date) {
        return membership.getVisits().stream()
                .filter(v -> v.getDate().getYear() == date.getYear()
                        && v.getDate().getMonth() == date.getMonth())
                .count() < monthlyLimit;
    }

    public String name() { return "Лимит " + monthlyLimit + " посещений в месяц"; }
}
