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

import com.nasrabadiam.rover.Callback;
import com.nasrabadiam.rover.DIRECTION;
import com.nasrabadiam.rover.Path;
import com.nasrabadiam.rover.Position;
import com.nasrabadiam.rover.Rover;
import com.nasrabadiam.rover.model.RemoteDataServiceProvider;
import com.nasrabadiam.rover.model.RoverModel;
import com.nasrabadiam.rover.model.RoverModelImpl;
import com.nasrabadiam.rover.model.RoverResponseModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainPresenter implements MainContract.Presenter {

    private MainContract.View view = null;
    private RoverModel model;
    private ExecutorService executorService = Executors.newCachedThreadPool();
    private Rover lastRover;
    private Position lastRoverPosition;
    private DIRECTION lastCurrentPosDirection=DIRECTION.TOP;
    private List<Pair<Path, Position>> paths = new ArrayList<>();

    public static MainPresenter getPresenter() {
        return new MainPresenter(new RoverModelImpl(new RemoteDataServiceProvider()));
    }

    private MainPresenter(RoverModel roverModel) {
        this.model = roverModel;
        lastRover = new Rover(DIRECTION.TOP);
    }

    @Override
    public void setView(MainContract.View view) {
        this.view = view;
    }

    @Override
    public void onStop() {
        executorService.shutdown();
    }

    @Override
    public void getNewRover() {
        resetWorld();
        view.hideLoadingError();
        view.hideRover();
        view.showLoading();
        view.hideLand();
        model.getNewRover(new Callback<RoverResponseModel>() {

            @Override
            public void onSuccess(final RoverResponseModel response) {
                lastRoverPosition = response.startPoint;

                view.hideLoading();
                view.showLand();
                view.resetLand();
                //set Rover Start Point
                setRoverStartPoint(response.startPoint);

                //set weirs
                setWeirs(response.weirs);

                //run command
                runCommand(response);
            }

            @Override
            public void onError(Throwable throwable) {
                view.hideLand();
                view.hideLoading();
                view.showLoadingError(throwable.getMessage());
            }
        });
    }

    private void resetWorld() {
        lastRover = new Rover(DIRECTION.TOP);
        lastRoverPosition = null;
        paths.clear();
    }

    private void setRoverStartPoint(Position position) {
        Rover initRover = new Rover(DIRECTION.TOP);
        view.showRover(initRover, position);
    }

    private void setWeirs(List<Position> weirs) {
        view.showWeirs(weirs);
    }

    private void runCommand(final RoverResponseModel roverResponseModel) {
        executorService = Executors.newCachedThreadPool();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                String command = roverResponseModel.command;

                for (int i = 0; i < command.length(); i++) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    char currentCommand = command.charAt(i);

                    for (int index = 0; index < roverResponseModel.weirs.size(); index++) {
                        if (roverResponseModel.weirs.get(index).equals(lastRoverPosition)) {
                            view.showRoverCrashWithWeirs(lastRoverPosition);
                            return;
                        }
                    }

                    switch (currentCommand) {
                        case 'M':
                            Position nextRoverPosition;
                            switch (lastRover.getDirection()) {
                                case BOTTOM:
                                    nextRoverPosition = new Position(lastRoverPosition.getX(),
                                            lastRoverPosition.getY() - 1);
                                    break;
                                case RIGHT:
                                    nextRoverPosition = new Position(lastRoverPosition.getX() + 1,
                                            lastRoverPosition.getY());
                                    break;
                                case LEFT:
                                    nextRoverPosition = new Position(lastRoverPosition.getX() - 1,
                                            lastRoverPosition.getY());
                                    break;
                                case TOP:
                                    nextRoverPosition = new Position(lastRoverPosition.getX(),
                                            lastRoverPosition.getY() + 1);
                                    break;
                                default:
                                    nextRoverPosition = new Position(lastRoverPosition.getX(),
                                            lastRoverPosition.getY());
                                    break;
                            }

                            paths.add(new Pair<>(new Path(lastCurrentPosDirection, lastRover.getDirection()), lastRoverPosition));
                            view.showPath(paths);
                            lastCurrentPosDirection = lastRover.getDirection();


                            if (nextRoverPosition.getX() < 0 || nextRoverPosition.getX() >= 10 ||
                                    nextRoverPosition.getY() < 0 || nextRoverPosition.getY() >= 20) {
                                view.showRoverOutOfLandError(nextRoverPosition);
                                return;
                            }
                            view.showRover(lastRover, nextRoverPosition);
                            lastRoverPosition = nextRoverPosition;

                            break;
                        case 'R':
                            lastRover.turnRight();
                            view.showRover(lastRover, lastRoverPosition);

                            break;
                        case 'L':
                            lastRover.turnLeft();
                            view.showRover(lastRover, lastRoverPosition);

                            break;
                    }
                }
            }
        });
    }

}
