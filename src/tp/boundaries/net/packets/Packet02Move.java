package tp.boundaries.net.packets;

import tp.boundaries.net.GameClient;
import tp.boundaries.net.GameServer;

public class Packet02Move extends Packet {

    private String username;
    private int hp, pLevel, x, y ,uID;

    private int numSteps = 0;
    private boolean isMoving;
    private int movingDir = 1;

    public Packet02Move(byte[] data) {
        super(02);
        String[] dataArray = readData(data).split(",");
        this.uID = Integer.parseInt(dataArray[0]);
        this.username = dataArray[1];
        this.hp = Integer.parseInt(dataArray[2]);
        this.pLevel = Integer.parseInt(dataArray[3]);
        this.x = Integer.parseInt(dataArray[4]);
        this.y = Integer.parseInt(dataArray[5]);
        this.numSteps = Integer.parseInt(dataArray[6]);
        this.isMoving = Integer.parseInt(dataArray[7]) == 1;
        this.movingDir = Integer.parseInt(dataArray[8]);
    }

    public Packet02Move(int uID, String username, int hp, int pLevel, int x, int y, int numSteps, boolean isMoving, int movingDir) {
        super(02);
        this.uID = uID;
        this.username = username;
        this.hp = hp;
        this.pLevel = pLevel;
        this.x = x;
        this.y = y;
        this.numSteps = numSteps;
        this.isMoving = isMoving;
        this.movingDir = movingDir;
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
        return ("02" + this.uID + "," + this.username + "," + this.hp + "," + this.pLevel + "," + this.x + "," + this.y + "," + this.numSteps + "," + (isMoving ? 1 : 0)
                + "," + this.movingDir).getBytes();

    }
    
    public int getUID(){
    	return this.uID;
    }

    public String getUsername() {
        return username;
    }
    
    public int getHp() {
    	return this.hp;
    }
    
    public int getpLevel() {
    	return this.pLevel;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getNumSteps() {
        return numSteps;
    }

    public boolean isMoving() {
        return isMoving;
    }

    public int getMovingDir() {
        return movingDir;
    }
}
