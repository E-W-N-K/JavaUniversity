package by.sakun.fitness.service;

import by.sakun.fitness.factory.MembershipFactory;
import by.sakun.fitness.model.Client;
import by.sakun.fitness.model.Membership;
import by.sakun.fitness.model.Visit;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public final class FitnessClub {
    private final Map<Long, Client> clients = new LinkedHashMap<>();
    private final Map<Long, Membership> memberships = new LinkedHashMap<>();
    private final Notifier notifier;
    private long nextClientId = 1;
    private long nextMembershipId = 1;
    private long nextVisitId = 1;

    public FitnessClub(Notifier notifier) { this.notifier = Objects.requireNonNull(notifier); }

    public Client addClient(String name) {
        Client client = new Client(nextClientId, name);
        clients.put(nextClientId++, client);
        return client;
    }

    public Membership issueMembership(long clientId, int months, MembershipFactory factory) {
        Client client = getClient(clientId);
        Objects.requireNonNull(factory, "Фабрика обязательна");
        Membership membership = factory.create(nextMembershipId, client, months);
        memberships.put(nextMembershipId++, membership);
        return membership;
    }

    public void activate(long membershipId, LocalDate date) {
        Membership membership = getMembership(membershipId);
        membership.activate(date);
        notifier.send("Абонемент №" + membershipId + " активирован для " + membership.getClient().getName());
    }

    public Visit recordVisit(long membershipId, LocalDate date) {
        Visit visit = getMembership(membershipId).recordVisit(nextVisitId, date);
        nextVisitId++;
        notifier.send("Посещение №" + visit.getId() + " учтено");
        return visit;
    }

    public Client getClient(long id) {
        Client client = clients.get(id);
        if (client == null) throw new IllegalArgumentException("Клиент не найден: " + id);
        return client;
    }

    public Membership getMembership(long id) {
        Membership membership = memberships.get(id);
        if (membership == null) throw new IllegalArgumentException("Абонемент не найден: " + id);
        return membership;
    }

    public java.util.Collection<Client> getClients() { return java.util.List.copyOf(clients.values()); }
    public java.util.Collection<Membership> getMemberships() { return java.util.List.copyOf(memberships.values()); }
}
