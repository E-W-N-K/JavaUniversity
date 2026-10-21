package by.sakun.fitness.model;

public final class Client {
    private final long id;
    private final String name;

    public Client(long id, String name) {
        if (id <= 0) throw new IllegalArgumentException("ID клиента должен быть положительным");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Имя клиента не может быть пустым");
        this.id = id;
        this.name = name.trim();
    }

    public long getId() { return id; }
    public String getName() { return name; }
}
