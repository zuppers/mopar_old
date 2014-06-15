/**
 * Copyright (c) 2012, Hadyn Richard
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy 
 * of this software and associated documentation files (the "Software"), to deal 
 * in the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in 
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR 
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL 
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER 
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN 
 * THE SOFTWARE.
 */

package net.scapeemulator.game.msg.impl;

import net.scapeemulator.game.model.Position;
import net.scapeemulator.game.model.mob.Mob;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.msg.Message;

/**
 * Created by Hadyn Richard
 */
public final class CreateProjectileMessage extends Message {
    
	private final int deltaX;
	private final int deltaY;
	private final int lockon;
	private final int graphic;
	private final int startHeight;
	private final int endHeight;
	private final int startSpeed;
	private final int speed;
	private final int angle;
	private final int arc;
	
    public CreateProjectileMessage(Position source, Position destination, Mob lockon, int graphic, int startHeight, int endHeight, int startSpeed, int speed) {
    	deltaX = (source.getX() - destination.getX()) * -1;
    	deltaY = (source.getY() - destination.getY()) * -1;
    	this.lockon = lockon instanceof Player ? (-lockon.getId() - 1) : (lockon.getId() + 1);
    	this.graphic = graphic;
    	this.startHeight = startHeight;
    	this.endHeight = endHeight;
    	this.startSpeed = startSpeed;
    	this.speed = speed;
        this.angle = 50;
        this.arc = 16;
    }
    
	public int getDeltaX() {
		return deltaX;
	}

	public int getDeltaY() {
		return deltaY;
	}

	public int getLockon() {
		return lockon;
	}

	public int getGraphic() {
		return graphic;
	}

	public int getStartHeight() {
		return startHeight;
	}

	public int getEndHeight() {
		return endHeight;
	}

	public int getStartSpeed() {
		return startSpeed;
	}

	public int getSpeed() {
		return speed;
	}

	public int getAngle() {
		return angle;
	}
	
	public int getArc() {
		return arc;
	}
}
