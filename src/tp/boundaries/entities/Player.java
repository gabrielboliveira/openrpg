package tp.boundaries.entities;

import tp.boundaries.Game;
import tp.boundaries.InputHandler;
import tp.boundaries.gfx.Colours;
import tp.boundaries.gfx.Font;
import tp.boundaries.gfx.Screen;
import tp.boundaries.level.Level;
import tp.boundaries.net.packets.Packet02Move;

public class Player extends Mob {

    private InputHandler input;
    private int colour = Colours.get(-1, 111, 100, 543);
    private int scale = 1;
    protected boolean isSwimming = false;
    protected boolean isLavaing = false;
    private int tickCount = 0;
    private String username;

    public Player(Level level, int uID, int x, int y, InputHandler input, String username, int hp, int pLevel) {
        super(level, uID, "Player", hp, pLevel, x, y, 1);
        this.input = input;
        this.username = username;
    }

    public void tick() {
        int xa = 0;
        int ya = 0;
        if (input != null) {
            if (input.up.isPressed()) {
                ya--;
            }
            if (input.down.isPressed()) {
                ya++;
            }
            if (input.left.isPressed()) {
                xa--;
            }
            if (input.right.isPressed()) {
                xa++;
            }
        }
        if (xa != 0 || ya != 0) {
            move(xa, ya);
            isMoving = true;

            Packet02Move packet = new Packet02Move(this.getUID(), this.getUsername(), this.getHp(), this.getpLevel(), this.x, this.y, this.numSteps, this.isMoving,
                    this.movingDir);
            packet.writeData(Game.game.socketClient);
        } else {
            isMoving = false;
        }
        if (level.getTile(this.x >> 3, this.y >> 3).getId() == 3) {
            isSwimming = true;
        }
        if (isSwimming && level.getTile(this.x >> 3, this.y >> 3).getId() != 3) {
            isSwimming = false;
        }
        if (level.getTile(this.x >> 3, this.y >> 3).getId() == 4) {
            isLavaing = true;
            if (tickCount % 120 == 1)
            	hp -= 10;
        }
        if (isLavaing && level.getTile(this.x >> 3, this.y >> 3).getId() != 4) {
            isLavaing = false;
        }
        tickCount++;
    }

    public void render(Screen screen) {
        int xTile = 0;
        int yTile = 28;
        int walkingSpeed = 4;
        int flipTop = (numSteps >> walkingSpeed) & 1;
        int flipBottom = (numSteps >> walkingSpeed) & 1;

        if (movingDir == 1) {
            xTile += 2;
        } else if (movingDir > 1) {
            xTile += 4 + ((numSteps >> walkingSpeed) & 1) * 2;
            flipTop = (movingDir - 1) % 2;
        }

        int modifier = 8 * scale;
        int xOffset = x - modifier / 2;
        int yOffset = y - modifier / 2 - 4;
        
        if (isSwimming) {
            int waterColour = 0;
            yOffset += 4;
            if (tickCount % 60 < 15) {
                waterColour = Colours.get(-1, -1, 225, -1);
            } else if (15 <= tickCount % 60 && tickCount % 60 < 30) {
                yOffset -= 1;
                waterColour = Colours.get(-1, 225, 115, -1);
            } else if (30 <= tickCount % 60 && tickCount % 60 < 45) {
                waterColour = Colours.get(-1, 115, -1, 225);
            } else {
                yOffset -= 1;
                waterColour = Colours.get(-1, 225, 115, -1);
            }
            screen.render(xOffset, yOffset + 3, 0 + 27 * 32, waterColour, 0x00, 1);
            screen.render(xOffset + 8, yOffset + 3, 0 + 27 * 32, waterColour, 0x01, 1);
        }
        if (isLavaing) {
            int lavaColour = 0;
            yOffset += 4;
            if (tickCount % 60 < 15) {
                lavaColour = Colours.get(-1, -1, 496, -1);
            } else if (15 <= tickCount % 60 && tickCount % 60 < 30) {
                yOffset -= 1;
                lavaColour = Colours.get(-1, 496, 161, -1);
            } else if (30 <= tickCount % 60 && tickCount % 60 < 45) {
                lavaColour = Colours.get(-1, 161, -1, 496);
            } else {
                yOffset -= 1;
                lavaColour = Colours.get(-1, 496, 161, -1);
            }
            screen.render(xOffset, yOffset + 3, 0 + 27 * 32, lavaColour, 0x00, 1);
            screen.render(xOffset + 8, yOffset + 3, 0 + 27 * 32, lavaColour, 0x01, 1);
        }
        
        screen.render(xOffset + (modifier * flipTop), yOffset, xTile + yTile * 32, colour, flipTop, scale);
        screen.render(xOffset + modifier - (modifier * flipTop), yOffset, (xTile + 1) + yTile * 32, colour, flipTop, scale);

        if (!isSwimming && !isLavaing) {
            screen.render(xOffset + (modifier * flipBottom), yOffset + modifier, xTile + (yTile + 1) * 32, colour,
                    flipBottom, scale);
            screen.render(xOffset + modifier - (modifier * flipBottom), yOffset + modifier, (xTile + 1) + (yTile + 1)
                    * 32, colour, flipBottom, scale);
        }
        if (username != null) {
            Font.render(username, screen, xOffset - ((username.length() - 1) / 2 * 8), yOffset - 10,
                    Colours.get(-1, -1, -1, 555), 1);
        }
        
        if (hp > 0) {
            Font.render(Integer.toString(hp), screen, xOffset + 2 * username.length(), yOffset - 20,
                    Colours.get(-1, -1, 215, 511), 1);
        }
        
        if (pLevel != 0) {
            Font.render("Lv." + Integer.toString(pLevel) + " ", screen, xOffset - 5 * username.length(), yOffset - 20,
                    Colours.get(-1, -1, 215, 553), 1);
        }
        
    }

    public boolean hasCollided(int xa, int ya) {
        int xMin = 0;
        int xMax = 7;
        int yMin = 3;
        int yMax = 7;
        for (int x = xMin; x < xMax; x++) {
            if (isSolidTile(xa, ya, x, yMin)) {
                return true;
            }
        }
        for (int x = xMin; x < xMax; x++) {
            if (isSolidTile(xa, ya, x, yMax)) {
                return true;
            }
        }
        for (int y = yMin; y < yMax; y++) {
            if (isSolidTile(xa, ya, xMin, y)) {
                return true;
            }
        }
        for (int y = yMin; y < yMax; y++) {
            if (isSolidTile(xa, ya, xMax, y)) {
                return true;
            }
        }
        return false;
    }

    public String getUsername() {
        return this.username;
    }

}
