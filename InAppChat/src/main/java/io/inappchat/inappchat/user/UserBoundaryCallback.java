package io.inappchat.inappchat.user;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RestrictTo;
import androidx.paging.PagedList;

import io.inappchat.inappchat.cache.database.entity.User;
import io.inappchat.inappchat.cache.preference.PreferenceManager;
import io.inappchat.inappchat.remote.model.response.UserListResponse;
import io.inappchat.inappchat.remote.model.response.UserResponse;
import io.inappchat.inappchat.data.DataManager;
import io.inappchat.inappchat.utils.Logger;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import java.io.IOException;

import retrofit2.Response;

/**
 * @author meeth
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public class UserBoundaryCallback extends PagedList.BoundaryCallback<User> {

    private final PreferenceManager preferenceManager;
    private DataManager dataManager;

    public UserBoundaryCallback(DataManager dataManager) {
        this.preferenceManager = dataManager.preference();
        this.dataManager = dataManager;
    }

    private boolean isLoading = false;
    private String lastId = null;

    private Disposable disposable;

    @Override
    public void onZeroItemsLoaded() {
        Logger.e("UserBoundaryCallback", "onZeroItemsLoaded");
        super.onZeroItemsLoaded();
        loadUsersFromRemote();
    }

    @Override
    public void onItemAtFrontLoaded(@NonNull User itemAtFront) {
        Log.e("UserBoundaryCallback", "onItemAtFrontLoaded");
        super.onItemAtFrontLoaded(itemAtFront);
    }

    @Override
    public void onItemAtEndLoaded(@NonNull User itemAtEnd) {
        Log.e("UserBoundaryCallback", "onItemAtEndLoaded");
        super.onItemAtEndLoaded(itemAtEnd);
        loadUsersFromRemote();
    }

    private void loadUsersFromRemote() {
        if (isLoading) return;

        if (disposable != null) {
            disposable.dispose();
        }

        isLoading = true;
        disposable =
                Single.fromCallable(
                                () -> {
                                    String newLastId = null;
                                    String tenantId = preferenceManager.getTenantId();
                                    if (tenantId != null) {
                                        try {
                                            Response<UserListResponse> response =
                                                    dataManager.network().api().getUsersInSync(tenantId, lastId).execute();
                                            if (response.isSuccessful() && response.body() != null) {
                                                UserListResponse body = response.body();
                                                for (UserResponse userResponse : body.getUserList()) {
                                                    Log.e("UserBoundaryCallback", "User : " + userResponse.toString());
                                                    newLastId = userResponse.getUserId();
                                                }
                                            }
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    return newLastId;
                                })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::onItemsLoadSuccess, this::onItemsLoadError);
    }

    private void onItemsLoadSuccess(String lastId) {
        this.lastId = lastId;
        isLoading = false;
    }

    private void onItemsLoadError(Throwable throwable) {
        isLoading = false;
    }
}
