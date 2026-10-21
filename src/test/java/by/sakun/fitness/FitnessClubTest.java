package by.sakun.fitness;

import by.sakun.fitness.factory.*;
import by.sakun.fitness.model.*;
import by.sakun.fitness.service.*;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FitnessClubTest {
    private final LocalDate day = LocalDate.of(2026, 10, 21);

    @Test void notifierIsMockedAndCalledOnActivation() {
        Notifier notifier = mock(Notifier.class);
        FitnessClub club = new FitnessClub(notifier);
        Client client = club.addClient("Анна");
        Membership membership = club.issueMembership(client.getId(), 1, new LimitedMembershipFactory());
        club.activate(membership.getId(), day);
        verify(notifier).send("Абонемент №1 активирован для Анна");
    }

    @Test void integrationOfRealClubAndMembership() {
        FitnessClub club = new FitnessClub(message -> {});
        Client client = club.addClient("Иван");
        Membership membership = club.issueMembership(client.getId(), 1, new UnlimitedMembershipFactory());
        club.activate(membership.getId(), day);
        Visit visit = club.recordVisit(membership.getId(), day);
        assertAll(() -> assertEquals(1, visit.getId()),
                () -> assertEquals(1, club.getMembership(membership.getId()).getVisits().size()),
                () -> assertEquals("Иван", membership.getClient().getName()));
    }

    @Test void missingClientIsRejected() {
        FitnessClub club = new FitnessClub(message -> {});
        assertThrows(IllegalArgumentException.class, () -> club.issueMembership(99, 1, new LimitedMembershipFactory()));
    }
}
