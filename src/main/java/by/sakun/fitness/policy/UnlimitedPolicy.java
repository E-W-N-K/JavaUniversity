package by.sakun.fitness.policy;

import by.sakun.fitness.model.Membership;
import java.time.LocalDate;

public final class UnlimitedPolicy implements VisitPolicy {
    public boolean canVisit(Membership membership, LocalDate date) { return true; }
    public String name() { return "Безлимитный"; }
}
