package tp.boundaries.entities;

import tp.boundaries.level.Level;
import tp.boundaries.level.tiles.Tile;

public abstract class Mob extends Entity {

	protected int uID;
    protected String name;
    protected int speed;
    public int hp = 100;
    protected int pLevel = 1;
    protected int numSteps = 0;
    protected boolean isMoving;
    protected int movingDir = 1;
    protected int scale = 1;

    public Mob(Level level, int uID, String name, int hp, int pLevel, int x, int y, int speed) {
        super(level);
        this.uID = uID;
        this.name = name;
        this.hp = hp;
        this.pLevel = pLevel;
        this.x = x;
        this.y = y;
        this.speed = speed;
    }

    public void move(int xa, int ya) {
        if (xa != 0 && ya != 0) {
            move(xa, 0);
            move(0, ya);
            numSteps--;
            return;
        }
        numSteps++;
        if (!hasCollided(xa, ya)) {
            if (ya < 0)
                movingDir = 0;
            if (ya > 0)
                movingDir = 1;
            if (xa < 0)
                movingDir = 2;
            if (xa > 0)
                movingDir = 3;
            x += xa * speed;
            y += ya * speed;
        }
    }

    public abstract boolean hasCollided(int xa, int ya);

    protected boolean isSolidTile(int xa, int ya, int x, int y) {
        if (level == null) {
            return false;
        }
        Tile lastTile = level.getTile((this.x + x) >> 3, (this.y + y) >> 3);
        Tile newTile = level.getTile((this.x + x + xa) >> 3, (this.y + y + ya) >> 3);
        if (!lastTile.equals(newTile) && newTile.isSolid()) {
            return true;
        }
        return false;
    }

    public String getName() {
        return name;
    }
    
    public int getHp() {
        return this.hp;
    }
    
    public int getpLevel() {
    	return pLevel;
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

    public void setNumSteps(int numSteps) {
        this.numSteps = numSteps;
    }

    public void setMoving(boolean isMoving) {
        this.isMoving = isMoving;
    }

    public void setMovingDir(int movingDir) {
        this.movingDir = movingDir;
    }

    public int getUID(){
    	return this.uID;
    }
    
    public void setUID(int uID)
    {
    	this.uID = uID;
    }
}
