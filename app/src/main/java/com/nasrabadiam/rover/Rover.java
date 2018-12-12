/*
 *     This is the source code of rover project.
 *     Copyright (C)   Ali Nasrabadi  2018-2018
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.nasrabadiam.rover;

public class Rover {

    private DIRECTION direction;

    public Rover(DIRECTION direction) {
        this.direction = direction;
    }

    public DIRECTION getDirection() {
        return direction;
    }

    public void turnLeft() {
        if (direction == DIRECTION.TOP) {
            direction = DIRECTION.LEFT;
        } else if (direction == DIRECTION.LEFT) {
            direction = DIRECTION.BOTTOM;
        } else if (direction == DIRECTION.BOTTOM) {
            direction = DIRECTION.RIGHT;
        } else if (direction == DIRECTION.RIGHT) {
            direction = DIRECTION.TOP;
        }
    }

    public void turnRight() {
        if (direction == DIRECTION.TOP) {
            direction = DIRECTION.RIGHT;
        } else if (direction == DIRECTION.RIGHT) {
            direction = DIRECTION.BOTTOM;
        } else if (direction == DIRECTION.BOTTOM) {
            direction = DIRECTION.LEFT;
        } else if (direction == DIRECTION.LEFT) {
            direction = DIRECTION.TOP;
        }
    }
}
