package by.sakun.fitness.factory;

import by.sakun.fitness.policy.LimitedPolicy;
import by.sakun.fitness.policy.VisitPolicy;

public final class LimitedMembershipFactory extends MembershipFactory {
    protected VisitPolicy createPolicy() { return new LimitedPolicy(8); }
}
