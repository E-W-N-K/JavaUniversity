package by.sakun.fitness;

import by.sakun.fitness.factory.LimitedMembershipFactory;
import by.sakun.fitness.factory.MembershipFactory;
import by.sakun.fitness.factory.UnlimitedMembershipFactory;
import by.sakun.fitness.model.Membership;
import by.sakun.fitness.service.ConsoleNotifier;
import by.sakun.fitness.service.FitnessClub;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public final class Main {
    private final Scanner input = new Scanner(System.in);
    private final FitnessClub club = new FitnessClub(new ConsoleNotifier());

    public static void main(String[] args) { new Main().run(); }

    private void run() {
        while (true) {
            System.out.println("\nФИТНЕС-КЛУБ: 1 — клиент, 2 — абонемент, 3 — активация, 4 — посещение, 5 — список, 0 — выход");
            String choice = input.nextLine().trim();
            if (choice.equals("0")) { System.out.println("Работа завершена."); return; }
            try {
                switch (choice) {
                    case "1" -> addClient();
                    case "2" -> issueMembership();
                    case "3" -> activate();
                    case "4" -> recordVisit();
                    case "5" -> showAll();
                    default -> System.out.println("Выберите пункт от 0 до 5.");
                }
            } catch (IllegalArgumentException | IllegalStateException ex) {
                System.out.println("Ошибка: " + ex.getMessage());
            }
        }
    }

    private void addClient() {
        System.out.print("Имя клиента: ");
        System.out.println("Клиент создан, ID: " + club.addClient(input.nextLine()).getId());
    }

    private void issueMembership() {
        long clientId = readLong("ID клиента: ");
        long requestedMonths = readLong("Срок в месяцах: ");
        if (requestedMonths <= 0 || requestedMonths > 120) {
            throw new IllegalArgumentException("Срок должен быть от 1 до 120 месяцев");
        }
        int months = (int) requestedMonths;
        System.out.print("Тип (1 — безлимитный, 2 — 8 посещений в месяц): ");
        String type = input.nextLine().trim();
        MembershipFactory factory = switch (type) {
            case "1" -> new UnlimitedMembershipFactory();
            case "2" -> new LimitedMembershipFactory();
            default -> throw new IllegalArgumentException("Неизвестный тип абонемента");
        };
        Membership membership = club.issueMembership(clientId, months, factory);
        System.out.println("Абонемент №" + membership.getId() + " создан: " + membership.getPlanName());
    }

    private void activate() {
        long id = readLong("ID абонемента: ");
        club.activate(id, readDate("Дата активации (ГГГГ-ММ-ДД, Enter — сегодня): "));
        System.out.println("Действует до " + club.getMembership(id).getEndDate());
    }

    private void recordVisit() {
        long id = readLong("ID абонемента: ");
        club.recordVisit(id, readDate("Дата посещения (ГГГГ-ММ-ДД, Enter — сегодня): "));
        System.out.println("Всего посещений: " + club.getMembership(id).getVisits().size());
    }

    private void showAll() {
        System.out.println("Клиенты:");
        club.getClients().forEach(c -> System.out.println(c.getId() + " — " + c.getName()));
        System.out.println("Абонементы:");
        club.getMemberships().forEach(m -> System.out.println(m.getId() + " — " + m.getClient().getName()
                + ", " + m.getPlanName() + ", " + m.getStatus() + ", посещений: " + m.getVisits().size()));
    }

    private long readLong(String prompt) {
        System.out.print(prompt);
        try { return Long.parseLong(input.nextLine().trim()); }
        catch (NumberFormatException ex) { throw new IllegalArgumentException("Введите целое число"); }
    }

    private LocalDate readDate(String prompt) {
        System.out.print(prompt);
        String value = input.nextLine().trim();
        try { return value.isEmpty() ? LocalDate.now() : LocalDate.parse(value); }
        catch (DateTimeParseException ex) { throw new IllegalArgumentException("Дата должна иметь формат ГГГГ-ММ-ДД"); }
    }
}
