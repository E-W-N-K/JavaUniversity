package by.sakun.fitness.factory;

import by.sakun.fitness.policy.UnlimitedPolicy;
import by.sakun.fitness.policy.VisitPolicy;

public final class UnlimitedMembershipFactory extends MembershipFactory {
    protected VisitPolicy createPolicy() { return new UnlimitedPolicy(); }
}
