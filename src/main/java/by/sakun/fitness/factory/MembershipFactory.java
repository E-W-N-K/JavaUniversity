package by.sakun.fitness.factory;

import by.sakun.fitness.model.Client;
import by.sakun.fitness.model.Membership;
import by.sakun.fitness.policy.VisitPolicy;

public abstract class MembershipFactory {
    public Membership create(long id, Client client, int months) {
        return new Membership(id, client, months, createPolicy());
    }

    protected abstract VisitPolicy createPolicy();
}
