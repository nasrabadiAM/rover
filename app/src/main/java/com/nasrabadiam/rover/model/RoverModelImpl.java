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

import android.util.Log;

import com.nasrabadiam.rover.Callback;

import retrofit2.Call;
import retrofit2.Response;

public class RoverModelImpl implements RoverModel {

    private RemoteDataServiceProvider provider;

    public RoverModelImpl(RemoteDataServiceProvider provider) {
        this.provider = provider;
    }

    @Override
    public void getNewRover(final Callback<RoverResponseModel> callback) {
        Call<RoverResponseModel> call = provider.roverService.getNewRover("12856497");
        call.enqueue(new retrofit2.Callback<RoverResponseModel>() {
            @Override
            public void onResponse(Call<RoverResponseModel> call, Response<RoverResponseModel> response) {
                if (response.isSuccessful()) {
                    Log.e("REEVRover", "response=" + response.body().toString());
                    callback.onSuccess(response.body());
                } else {
                    callback.onError(new Throwable("unSuccessful response!"));
                }
            }

            @Override
            public void onFailure(Call<RoverResponseModel> call, Throwable t) {
                callback.onError(t);
            }
        });
    }
}
