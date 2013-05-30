package tp.boundaries.net.packets;

import tp.boundaries.net.GameClient;
import tp.boundaries.net.GameServer;

public class Packet01Disconnect extends Packet {

    private int uID;

    public Packet01Disconnect(byte[] data) {
        super(01);
        this.uID = Integer.parseInt(readData(data));
    }

    public Packet01Disconnect(int uID) {
        super(01);
        this.uID = uID;
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
        return ("01" + this.uID).getBytes();
    }

    public int getUID() {
        return this.uID;
    }

}
