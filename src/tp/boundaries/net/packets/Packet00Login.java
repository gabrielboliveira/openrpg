package tp.boundaries.net.packets;

import tp.boundaries.net.GameClient;
import tp.boundaries.net.GameServer;

public class Packet00Login extends Packet {

    private String username;
    private int x, y, hp, pLevel;

    public Packet00Login(byte[] data) {
        super(00);
        String[] dataArray = readData(data).split(",");
        this.username = dataArray[0];
        this.hp = Integer.parseInt(dataArray[1]);
        this.pLevel = Integer.parseInt(dataArray[2]);
        this.x = Integer.parseInt(dataArray[3]);
        this.y = Integer.parseInt(dataArray[4]);
    }

    public Packet00Login(String username, int hp, int pLevel, int x, int y) {
        super(00);
        this.username = username;
        this.hp = hp;
        this.pLevel = pLevel;
        this.x = x;
        this.y = y;
    }

    @Override
    public void writeData(GameClient client) {
        client.sendData(getData());
    }

    @Override
    public void writeData(GameServer server) {
        server.sendDataToAllClients(getData());
    }

    @Override
    public byte[] getData() {
        return ("00" + this.username + "," + getHp() + "," + getpLevel() + "," + getX() + "," + getY()).getBytes();
    }

    public String getUsername() {
        return username;
    }
    
    public int getHp() {
        return hp;
    }
    
    public int getpLevel () {
    	return pLevel;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

}
