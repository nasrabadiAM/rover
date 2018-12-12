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

package com.nasrabadiam.rover.ui;

import android.util.Pair;

import com.nasrabadiam.rover.Path;
import com.nasrabadiam.rover.Position;
import com.nasrabadiam.rover.Rover;

import java.util.List;

public class MainContract {
    interface Presenter {
        void setView(View view);

        void detachView();

        void getNewRover();
    }

    interface View {
        void showLand();

        void hideLand();

        void showRover(Rover rover, Position position);

        void hideRover();

        void showLoading();

        void hideLoading();

        void showLoadingError(String message);

        void hideLoadingError();

        void showWeirs(List<Position> weirs);

        void resetLand();

        void showRoverCrashWithWeirs(Position lastRoverPosition);

        void showPath(List<Pair<Path, Position>> paths);

        void showRoverOutOfLandError(Position nextRoverPosition);
    }
}
