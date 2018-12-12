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

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.nasrabadiam.rover.MarsView;
import com.nasrabadiam.rover.Path;
import com.nasrabadiam.rover.Position;
import com.nasrabadiam.rover.R;
import com.nasrabadiam.rover.Rover;

import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements MainContract.View {

    private MarsView marsView;
    private ProgressBar progressBar;
    private ImageView reload;
    private MainContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        marsView = findViewById(R.id.mars_view);
        progressBar = findViewById(R.id.progress_bar);
        reload = findViewById(R.id.reload);
        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.getNewRover();
            }
        });

        presenter = MainPresenter.getPresenter();
        presenter.setView(this);
        presenter.getNewRover();
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.detachView();
        presenter = null;
    }

    @Override
    public void showLand() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                marsView.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void hideLand() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                marsView.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void showRover(final Rover rover, final Position position) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                marsView.showRover(rover, position);
            }
        });
    }

    @Override
    public void hideRover() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                marsView.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void showLoading() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void hideLoading() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void showWeirs(final List<Position> weirs) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                marsView.setWeirs(weirs);
            }
        });
    }

    @Override
    public void resetLand() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                marsView.resetView();
            }
        });
    }

    @Override
    public void showRoverCrashWithWeirs(final Position lastRoverPosition) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setMessage(getString(R.string.rover_crash) + " \nx=" + lastRoverPosition.getX() + "\ny=" + lastRoverPosition.getY());
                dialog.setNeutralButton(getString(R.string.ok),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                dialog.show();
            }
        });
    }

    @Override
    public void showPath(List<Pair<Path, Position>> paths) {
        marsView.setPaths(paths);
    }

    @Override
    public void showRoverOutOfLandError(Position nextRoverPosition) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setMessage(getString(R.string.out_of_land));
                dialog.setNeutralButton(getString(R.string.ok),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                dialog.show();
            }
        });
    }

    @Override
    public void showLoadingError(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                reload.setVisibility(View.VISIBLE);
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setMessage(message);
                dialog.setNeutralButton(getString(R.string.ok),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                dialog.show();
            }
        });
    }

    @Override
    public void hideLoadingError() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                reload.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.reload) {
            presenter.getNewRover();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
