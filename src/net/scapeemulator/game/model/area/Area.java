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

package net.scapeemulator.game.model.area;

import net.scapeemulator.game.model.Position;

/**
 * Created by Hadyn Richard
 */
public abstract class Area {

    /**
     * Returns whether or not all of the defined tiles (bottom left corner defined by position,
     * length and width by offset) are within this area.
     * 
     * @param position The bottom left corner of the area to check
     * @param offset The area length and width
     * @return true if the square area is entirely within the specified area.
     */
    public boolean allWithinArea(Position position, int offset, int padding) {
        for (int xOffset = 0; xOffset < offset; xOffset++) {
            for (int yOffset = 0; yOffset < offset; yOffset++) {

                /* Check if the position at the new x and y offset is within the area */
                if (!withinArea(position.getX() + xOffset, position.getY() + yOffset, padding)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Returns whether or not any of the defined tiles (bottom left corner defined by position,
     * length and width by offset) are within this area.
     * 
     * @param position The bottom left corner of the area to check
     * @param offset The area length and width
     * @return true if the square area is at least partially within the specified area.
     */
    public boolean anyWithinArea(Position position, int offset, int padding) {
        for (int xOffset = 0; xOffset < offset; xOffset++) {
            for (int yOffset = 0; yOffset < offset; yOffset++) {

                /* Check if the position at the new x and y offset is within the area */
                if (withinArea(position.getX() + xOffset, position.getY() + yOffset, padding)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Gets if a certain coordinate is within the area.
     * 
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @param padding The padding around the area to also include in calculation
     * @return true if the coordinate is within the area
     */
    public abstract boolean withinArea(int x, int y, int padding);

    public abstract Position center();
    
    public abstract Position randomPosition(int height);
}
