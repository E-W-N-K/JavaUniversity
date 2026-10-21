package by.sakun.fitness;

import by.sakun.fitness.model.*;
import by.sakun.fitness.policy.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class ModelTest {
    private final LocalDate start = LocalDate.of(2026, 10, 1);

    @Test void clientStoresTrimmedName() { assertEquals("Анна", new Client(1, " Анна ").getName()); }
    @Test void clientRejectsEmptyName() { assertThrows(IllegalArgumentException.class, () -> new Client(1, " ")); }
    @Test void clientRejectsBadId() { assertThrows(IllegalArgumentException.class, () -> new Client(0, "Анна")); }
    @Test void visitStoresDate() { assertEquals(start, new Visit(1, start).getDate()); }
    @Test void visitRejectsBadId() { assertThrows(IllegalArgumentException.class, () -> new Visit(-1, start)); }
    @Test void visitRejectsNullDate() { assertThrows(NullPointerException.class, () -> new Visit(1, null)); }

    @Test void membershipActivatesForOneMonth() {
        Membership m = membership(new UnlimitedPolicy());
        m.activate(start);
        assertAll(() -> assertEquals(MembershipStatus.ACTIVE, m.getStatus()),
                () -> assertEquals(LocalDate.of(2026, 10, 31), m.getEndDate()));
    }

    @Test void membershipRejectsSecondActivation() {
        Membership m = membership(new UnlimitedPolicy());
        m.activate(start);
        assertThrows(IllegalStateException.class, () -> m.activate(start));
    }

    @Test void membershipRejectsVisitBeforeActivation() {
        assertThrows(IllegalStateException.class, () -> membership(new UnlimitedPolicy()).recordVisit(1, start));
    }

    @ParameterizedTest @ValueSource(ints = {1, 15, 31})
    void membershipAcceptsVisitsWithinPeriod(int day) {
        Membership m = membership(new UnlimitedPolicy());
        m.activate(start);
        assertEquals(1, m.recordVisit(1, start.withDayOfMonth(day)).getId());
    }

    @Test void membershipExpiresAfterEndDate() {
        Membership m = membership(new UnlimitedPolicy());
        m.activate(start);
        assertThrows(IllegalStateException.class, () -> m.recordVisit(1, LocalDate.of(2026, 11, 1)));
        assertEquals(MembershipStatus.EXPIRED, m.getStatus());
    }

    @Test void limitedPolicyStopsNinthVisit() {
        Membership m = membership(new LimitedPolicy(8));
        m.activate(start);
        for (int i = 1; i <= 8; i++) m.recordVisit(i, start);
        assertThrows(IllegalStateException.class, () -> m.recordVisit(9, start));
        assertEquals(8, m.getVisits().size());
    }

    @Test void visitsCannotBeChangedExternally() {
        Membership m = membership(new UnlimitedPolicy());
        assertThrows(UnsupportedOperationException.class, () -> m.getVisits().add(new Visit(1, start)));
    }

    private Membership membership(VisitPolicy policy) { return new Membership(1, new Client(1, "Анна"), 1, policy); }
}
