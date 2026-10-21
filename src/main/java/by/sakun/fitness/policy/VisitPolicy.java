package by.sakun.fitness.policy;

import by.sakun.fitness.model.Membership;
import java.time.LocalDate;

public interface VisitPolicy {
    boolean canVisit(Membership membership, LocalDate date);
    String name();
}
