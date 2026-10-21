package by.sakun.fitness.service;

public final class ConsoleNotifier implements Notifier {
    public void send(String message) { System.out.println("Уведомление: " + message); }
}
