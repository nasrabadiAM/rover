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

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.View;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class MarsView extends View {

    private static int HORIZONTAL_CELL_COUNT = 10;
    private static int VERTICAL_CELL_COUNT = 20;

    private int viewHeight;
    private int viewWidth;

    private int eachCellLength;
    private int eachCellHeight;

    private Paint linePaint = new Paint();
    private Paint roverPaint = new Paint();
    private Paint textPaint = new Paint();

    private Cell[][] cells = new Cell[HORIZONTAL_CELL_COUNT][VERTICAL_CELL_COUNT];

    public void resetView() {
        cells = new Cell[HORIZONTAL_CELL_COUNT][VERTICAL_CELL_COUNT];
        initCells();
    }

    public void showRover(Rover rover, Position position) {
        cells[position.getX()][position.getY()].insideCell = rover;
        invalidate();
    }

    public void setWeirs(List<Position> positions) {
        for (Position position : positions) {
            cells[position.getX()][position.getY()].insideCell = new Weir();
        }
        invalidate();
    }

    public void setPaths(List<Pair<Path, Position>> paths) {
        for (Pair pathAndPosition : paths) {
            Path path = (Path) pathAndPosition.first;
            Position position = (Position) pathAndPosition.second;
            cells[position.getX()][position.getY()].insideCell = path;
        }
        invalidate();
    }

    public MarsView(Context context) {
        super(context);
        init(context);
    }

    public MarsView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MarsView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MarsView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        viewWidth = MeasureSpec.getSize(widthMeasureSpec);
        viewHeight = MeasureSpec.getSize(heightMeasureSpec);

        eachCellLength = (viewWidth - getPaddingRight() - getPaddingLeft()) / HORIZONTAL_CELL_COUNT;
        eachCellHeight = (viewHeight - getPaddingTop() - getPaddingBottom()) / VERTICAL_CELL_COUNT;

        int heightPadding = getPaddingBottom() + getPaddingTop();
        int widthPadding = getPaddingLeft() + getPaddingRight();
        this.setMeasuredDimension(viewWidth + widthPadding, viewHeight + heightPadding);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void init(Context context) {
        linePaint.setColor(Color.DKGRAY);
        linePaint.setStrokeWidth(4);

        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(36);

        roverPaint.setColor(Color.RED);
        roverPaint.setStrokeWidth(4);
    }

    private void initCells() {
        int id = 0;
        for (int j = 0; j < VERTICAL_CELL_COUNT; j++) {
            int thisCellsStartY = ((VERTICAL_CELL_COUNT - j) * eachCellHeight);
            for (int i = 0; i < HORIZONTAL_CELL_COUNT; i++) {
                cells[i][j] = new Cell();

                cells[i][j].id = id;
                id++;

                cells[i][j].startX = i * eachCellLength;
                cells[i][j].startY = thisCellsStartY;
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawLand(canvas);
        drawCells(canvas);
        invalidate();
    }

    private void drawCells(Canvas canvas) {
        for (int j = 0; j < VERTICAL_CELL_COUNT; j++) {
            for (int i = 0; i < HORIZONTAL_CELL_COUNT; i++) {
                drawInsideCell(cells[i][j], canvas);
            }
        }
    }

    private void drawInsideCell(Cell cell, Canvas canvas) {
        Object insideCell = cell.insideCell;
        if (insideCell instanceof Path) {
            drawPathInsideCell(cell, canvas);
        } else if (insideCell instanceof Weir) {
            drawWeirInsideCell(cell, canvas);
        } else if (insideCell instanceof Rover) {
            drawRoverInsideCell(cell, canvas);
        }
    }

    private void drawPathInsideCell(Cell cell, Canvas canvas) {
        DIRECTION startFrom = ((Path) cell.insideCell).startFrom;
        DIRECTION endTo = ((Path) cell.insideCell).endTo;

        float centerX = cell.startX + (eachCellLength / 2);
        float centerY = cell.startY - (eachCellHeight / 2);

        float startFromX = 0;
        float startFromY = 0;

        float bottomX = cell.startX + (eachCellLength / 2);
        float bottomY = cell.startY;

        float leftX = cell.startX;
        float leftY = cell.startY - (eachCellHeight / 2);

        float rightX = cell.startX + eachCellLength;
        float rightY = cell.startY - (eachCellHeight / 2);

        float topX = cell.startX + (eachCellLength / 2);
        float topY = cell.startY - eachCellHeight;

        switch (startFrom) {
            case TOP:
                startFromX = bottomX;
                startFromY = bottomY;
                break;

            case LEFT:
                startFromX = rightX;
                startFromY = rightY;
                break;

            case RIGHT:
                startFromX = leftX;
                startFromY = leftY;
                break;

            case BOTTOM:
                startFromX = topX;
                startFromY = topY;
                break;
        }
        canvas.drawLine(startFromX, startFromY, centerX, centerY, roverPaint);

        float endToX = 0;
        float endToY = 0;
        switch (endTo) {
            case TOP:
                endToX = topX;
                endToY = topY;
                break;

            case LEFT:
                endToX = leftX;
                endToY = leftY;
                break;

            case RIGHT:
                endToX = rightX;
                endToY = rightY;
                break;

            case BOTTOM:
                endToX = bottomX;
                endToY = bottomY;
                break;
        }
        canvas.drawLine(centerX, centerY, endToX, endToY, roverPaint);
    }

    private void drawWeirInsideCell(Cell cell, Canvas canvas) {
        float centerX = cell.startX + (eachCellLength / 2);
        float centerY = cell.startY - (eachCellHeight / 2);
        canvas.drawText("#", centerX, centerY, textPaint);
    }

    private void drawRoverInsideCell(Cell cell, Canvas canvas) {
        float bottomX = cell.startX + (eachCellLength / 2);
        float bottomY = cell.startY - 10;

        float leftX = cell.startX + 10;
        float leftY = cell.startY - (eachCellHeight / 2);

        float rightX = cell.startX + eachCellLength - 10;
        float rightY = cell.startY - (eachCellHeight / 2);

        float topX = cell.startX + (eachCellLength / 2);
        float topY = cell.startY - eachCellHeight + 10;

        DIRECTION direction = ((Rover) cell.insideCell).getDirection();
        android.graphics.Path path = new android.graphics.Path();
        switch (direction) {
            case TOP:
                path.moveTo(topX, topY);
                path.lineTo(leftX, leftY);
                path.lineTo(rightX, rightY);
                path.lineTo(topX, topY);
                canvas.drawLine(bottomX,bottomY,topX,topY, roverPaint);
                break;
            case LEFT:
                path.moveTo(leftX, leftY);
                path.lineTo(topX, topY);
                path.lineTo(bottomX, bottomY);
                path.lineTo(leftX, leftY);
                canvas.drawLine(rightX,rightY,leftX,leftY, roverPaint);
                break;
            case RIGHT:
                path.moveTo(rightX, rightY);
                path.lineTo(topX, topY);
                path.lineTo(bottomX, bottomY);
                path.lineTo(rightX, rightY);
                canvas.drawLine(leftX,leftY,rightX,rightY, roverPaint);
                break;
            case BOTTOM:
                path.moveTo(bottomX, bottomY);
                path.lineTo(leftX, leftY);
                path.lineTo(rightX, rightY);
                path.lineTo(bottomX, bottomY);
                canvas.drawLine(topX,topY,bottomX,bottomY, roverPaint);
                break;
        }
        canvas.drawPath(path, roverPaint);
    }

    private void drawLand(Canvas canvas) {
        for (int i = 0; i < HORIZONTAL_CELL_COUNT + 1; i++) {
            canvas.drawLine(i * eachCellLength, 0, i * eachCellLength, viewHeight, linePaint);
        }
        for (int i = 0; i < VERTICAL_CELL_COUNT + 1; i++) {
            canvas.drawLine(0, i * eachCellHeight, viewWidth, i * eachCellHeight, linePaint);
        }
    }
}
