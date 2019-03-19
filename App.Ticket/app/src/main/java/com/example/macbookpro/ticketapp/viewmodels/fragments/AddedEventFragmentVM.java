package com.example.macbookpro.ticketapp.viewmodels.fragments;

import android.content.Context;

import com.example.macbookpro.ticketapp.helper.apiservice.ApiClient;
import com.example.macbookpro.ticketapp.helper.ultility.Ultil;
import com.example.macbookpro.ticketapp.models.ResponseMessage;
import com.example.macbookpro.ticketapp.models.TempEvent;
import com.example.macbookpro.ticketapp.models.User;
import com.example.macbookpro.ticketapp.models.UserInfor;
import com.example.macbookpro.ticketapp.models.UserParam;
import com.example.macbookpro.ticketapp.models.UserResponse;
import com.example.macbookpro.ticketapp.viewmodels.base.BaseFragmentVM;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Hoang Hai on 3/18/19.
 */
public class AddedEventFragmentVM extends BaseFragmentVM {

    private Context mContent;
    private AddedEventFragmentListened listened;
    public UserParam userParam;
    public List<TempEvent> addedEvents = new ArrayList<>();
    private User currentUser;

    public AddedEventFragmentVM(Context mContent, AddedEventFragmentListened listened) {
        this.mContent = mContent;
        this.listened = listened;
        currentUser = Ultil.getUserFromShardPreference(mContent);
    }

    public void getUserInfor() {
        final Call<UserResponse> userParamCall = ApiClient.getInstance().getApi().getUserInforById(currentUser.getId());
        userParamCall.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                UserResponse userResponse = response.body();
                userParam = userResponse.getUserParam();
                if (userParam != null) {
                    UserInfor.getInstance().setUserParam(userParam);
                    List<String> tempEvents = userParam.getOwnEvents();
                    Gson gson = new Gson();
                    addedEvents.clear();
                    for (String eventJson : tempEvents) {
                        TempEvent tempEvent = gson.fromJson(eventJson, TempEvent.class);
                        addedEvents.add(tempEvent);
                    }
                    listened.onGetApiSuccess();
                }

            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                listened.onGetApiFailed();

            }
        });
    }

    public void deleteEventById(String id, final int index) {
        final Call<ResponseMessage> deleteEventCall = ApiClient.getInstance().getApi().deleteEventById(id);
        deleteEventCall.enqueue(new Callback<ResponseMessage>() {
            @Override
            public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                listened.onDeleteEventSuccess(index);
            }

            @Override
            public void onFailure(Call<ResponseMessage> call, Throwable t) {
                listened.onDeleteEventFailed();
            }
        });
    }

    public void updateUserAfterDeleteEvent(int index) {
        addedEvents.remove(index);
        Gson gson = new Gson();
        userParam.getOwnEvents().clear();
        for (int i = 0; i < addedEvents.size(); i++) {
            TempEvent tempEvent = addedEvents.get(i);
            String tempEventJson = gson.toJson(tempEvent);
            userParam.setOwnEvents(tempEventJson);
        }
        if (userParam != null) {
            Call<ResponseMessage> uploadAddedEventCall = ApiClient.getInstance().getApi().updateUserInfor(userParam);
            uploadAddedEventCall.enqueue(new Callback<ResponseMessage>() {
                @Override
                public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                    listened.onUpdateUserInforSuccess();
                }

                @Override
                public void onFailure(Call<ResponseMessage> call, Throwable t) {
                    listened.onUpdateUserInforFailed();
                }
            });
        }
    }

    public interface AddedEventFragmentListened {
        void onGetApiSuccess();

        void onGetApiFailed();

        void onDeleteEventSuccess(int index);

        void onDeleteEventFailed();

        void onUpdateUserInforSuccess();

        void onUpdateUserInforFailed();
    }

}