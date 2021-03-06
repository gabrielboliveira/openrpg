package tp.boundaries.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import tp.boundaries.Game;
import tp.boundaries.entities.PlayerMP;
import tp.boundaries.net.packets.Packet;
import tp.boundaries.net.packets.Packet.PacketTypes;
import tp.boundaries.net.packets.Packet00Login;
import tp.boundaries.net.packets.Packet01Disconnect;
import tp.boundaries.net.packets.Packet02Move;

public class GameServer extends Thread {

    private DatagramSocket socket;
    private Game game;
    private List<PlayerMP> connectedPlayers = new ArrayList<PlayerMP>();
    private int port;

    public GameServer(Game game, int port) {
        this.game = game;
        this.port = port;
        try {
            this.socket = new DatagramSocket(port);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while (true) {
            byte[] data = new byte[1024];
            DatagramPacket packet = new DatagramPacket(data, data.length);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.parsePacket(packet.getData(), packet.getAddress(), packet.getPort());
        }
    }

    private void parsePacket(byte[] data, InetAddress address, int port) {
        String message = new String(data).trim();
        PacketTypes type = Packet.lookupPacket(message.substring(0, 2));
        Packet packet = null;
        switch (type) {
        default:
        case INVALID:
            break;
        case LOGIN:
            packet = new Packet00Login(data);
            System.out.println("[" + address.getHostAddress() + ":" + port + "] "
                    + ((Packet00Login) packet).getUsername() + " has connected...");
            PlayerMP player = new PlayerMP(game.level, ((Packet00Login) packet).getUID(), ((Packet00Login) packet).getUsername(), ((Packet00Login) packet).getX(), ((Packet00Login) packet).getY(), ((Packet00Login) packet).getHp(), ((Packet00Login) packet).getpLevel(), address, port);
            this.addConnection(player, (Packet00Login) packet);
            break;
        case DISCONNECT:
            packet = new Packet01Disconnect(data);
            System.out.println("[" + address.getHostAddress() + ":" + port + "] "
                    + ((Packet01Disconnect) packet).getUID() + " has left...");
            this.removeConnection((Packet01Disconnect) packet);
            break;
        case MOVE:
            packet = new Packet02Move(data);
            this.handleMove(((Packet02Move) packet));
        }
    }

    public void addConnection(PlayerMP player, Packet00Login packet) {
        boolean alreadyConnected = false;
        for (PlayerMP p : this.connectedPlayers) {
            if (player.getUID() == p.getUID()) {
                if (p.ipAddress == null) {
                    p.ipAddress = player.ipAddress;
                }
                if (p.port == -1) {
                    p.port = player.port;
                }
                alreadyConnected = true;
            } else {
                // relay to the current connected player that there is a new
                // player
                sendData(packet.getData(), p.ipAddress, p.port);
                //sendDataToAllClients(packet.getData());

                // relay to the new player that the currently connect player
                // exists
                Packet00Login packets = new Packet00Login(p.getUID(), p.getUsername(), p.getHp(), p.getpLevel(), p.x, p.y);
                sendData(packets.getData(), player.ipAddress, player.port);
            }
        }
        if (!alreadyConnected) {
        	//sendDataToAllClients(packet.getData());
            this.connectedPlayers.add(player);
        }
    }

    public void removeConnection(Packet01Disconnect packet) {
        this.connectedPlayers.remove(getPlayerMPIndex(packet.getUID()));
        packet.writeData(this);
    }

    public PlayerMP getPlayerMP(int uID) {
        for (PlayerMP player : this.connectedPlayers) {
            if (player.getUID() == uID) {
                return player;
            }
        }
        return null;
    }

    public int getPlayerMPIndex(int uID) {
        int index = 0;
        for (PlayerMP player : this.connectedPlayers) {
            if (player.getUID() == uID) {
                break;
            }
            index++;
        }
        return index;
    }

    public void sendData(byte[] data, InetAddress ipAddress, int port) {
        if (!game.isApplet) {
            DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, port);
            try {
                this.socket.send(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendDataToAllClients(byte[] data) {
        for (PlayerMP p : connectedPlayers) {
            sendData(data, p.ipAddress, p.port);
        }
    }

    private void handleMove(Packet02Move packet) {
        if (getPlayerMP(packet.getUID()) != null) {
            int index = getPlayerMPIndex(packet.getUID());
            PlayerMP player = this.connectedPlayers.get(index);
            player.setUID(packet.getUID());
            player.hp = packet.getHp();
            player.x = packet.getX();
            player.y = packet.getY();
            player.setMoving(packet.isMoving());
            player.setMovingDir(packet.getMovingDir());
            player.setNumSteps(packet.getNumSteps());
            packet.writeData(this);
        }
    }

}
