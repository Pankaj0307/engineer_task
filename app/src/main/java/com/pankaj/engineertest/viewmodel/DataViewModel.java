package com.pankaj.engineertest.viewmodel;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.pankaj.engineertest.model.DataModel;
import com.pankaj.engineertest.network.APIClient;
import com.pankaj.engineertest.network.APIInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataViewModel extends ViewModel {
    private MutableLiveData<DataModel> dataModelMutableLiveData;

    public DataViewModel() {
        dataModelMutableLiveData = new MutableLiveData<>();
        dataModelMutableLiveData.setValue(new DataModel());
    }

    public void clearModelData() {
        dataModelMutableLiveData.setValue(null);
    }

    public MutableLiveData<DataModel> getLiveData() {
        return dataModelMutableLiveData;
    }

    public void getApiData(final Context context, int page) {
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<DataModel> dataModelCall = apiInterface.getDataList(page);

        dataModelCall.enqueue(new Callback<DataModel>() {
            @Override
            public void onResponse(Call<DataModel> data, Response<DataModel> response) {
                if (response != null && response.body().getHits() != null) {
                    dataModelMutableLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<DataModel> call, Throwable t) {
                Toast.makeText(context, "Error while getting response", Toast.LENGTH_LONG).show();
            }
        });

    }

}
