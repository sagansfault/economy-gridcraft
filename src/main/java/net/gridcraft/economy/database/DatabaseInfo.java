package net.gridcraft.economy.database;

public class DatabaseInfo {

    private final String user;
    private final String password;
    private final String name;
    private final String port;
    private final String schema;

    public DatabaseInfo(String user, String password, String name, String port, String schema) {
        this.user = user;
        this.password = password;
        this.name = name;
        this.port = port;
        this.schema = schema;
    }

    public String url() {
        return "jdbc:mysql://" + this.name + ":" + this.port + "/" + this.schema;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }
}
