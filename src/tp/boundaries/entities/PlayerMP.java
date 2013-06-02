package tp.boundaries.entities;

import java.net.InetAddress;

import tp.boundaries.InputHandler;
import tp.boundaries.level.Level;

public class PlayerMP extends Player {

    public InetAddress ipAddress;
    public int port;

    public PlayerMP(Level level, int uID, String username, int x, int y, InputHandler input, int hp, int pLevel, InetAddress ipAddress, int port) {
        super(level, uID, x, y, input, username, hp, pLevel);
        this.ipAddress = ipAddress;
        this.port = port;
    }

    public PlayerMP(Level level, int uID, String username, int x, int y, int hp, int pLevel, InetAddress ipAddress, int port) {
        super(level, uID, x, y, null, username, hp, pLevel);
        this.ipAddress = ipAddress;
        this.port = port;
    }

    @Override
    public void tick() {
        super.tick();
    }
}