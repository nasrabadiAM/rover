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

package com.nasrabadiam.rover.model;

import com.google.gson.annotations.SerializedName;
import com.nasrabadiam.rover.Position;

import java.util.List;

import androidx.annotation.NonNull;

public class RoverResponseModel {
    @SerializedName("start_point")
    public Position startPoint;

    @SerializedName("weirs")
    public List<Position> weirs;

    @SerializedName("command")
    public String command;

    @NonNull
    @Override
    public String toString() {
        return "startPoint=" + startPoint +
                "\nweirs=" + weirs.toString() +
                "\ncommand=" + command;
    }
}
