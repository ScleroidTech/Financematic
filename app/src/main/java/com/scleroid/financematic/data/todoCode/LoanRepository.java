/*
package com.scleroid.financematic.data.todoCode;

*/
/**
 * Copyright (C) 2018
 *
 * @author Ganesh Kaple
 * @since 4/4/18
 *//*


import com.scleroid.financematic.data.local.model.Loan;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

*/
/**
 * Concrete implementation to load Loans from the data sources into a cache.
 * <p>
 * For simplicity, this implements a dumb synchronisation between locally persisted data and data
 * obtained from the server, by using the remote data source only if the local database doesn't
 * exist or is empty.
 * <p/>
 * By marking the constructor with {@code @Inject} and the class with {@code @Singleton}, Dagger
 * injects the dependencies required to create an instance of the LoansRespository (if it fails, it
 * emits a compiler error). It uses {@link LoansRepositoryModule} to do so, and the constructed
 * instance is available in {@link AppComponent}.
 * <p/>
 * Dagger generated code doesn't require public access to the constructor or class, and therefore,
 * to ensure the developer doesn't instantiate the class manually and bypasses Dagger, it's good
 * practice minimise the visibility of the class/constructor as much as possible.
 *//*

@Singleton
public class LoanRepository implements LoansDataSource {

    private final LoansDataSource mLoansRemoteDataSource;

    private final LoansDataSource mLoansLocalDataSource;

    */
/**
 * This variable has package local visibility so it can be accessed from tests.
 *//*

    Map<String, Loan> mCachedLoans;

    */
/**
 * Marks the cache as invalid, to force an update the next time data is requested. This variable
 * has package local visibility so it can be accessed from tests.
 *//*

    boolean mCacheIsDirty = false;

    */
/**
 * By marking the constructor with {@code @Inject
}, Dagger will try to inject the dependencies
 * required to create an instance of the LoanRepository. Because {@link LoansDataSource} is an
 * interface, we must provide to Dagger a way to build those arguments, this is done in {@link
 * LoansRepositoryModule}.
 * <p>
 * When two arguments or more have the same type, we must provide to Dagger a way to
 * differentiate them. This is done using a qualifier.
 * <p>
 * Dagger strictly enforces that arguments not marked with {@code @Nullable} are not injected
 * with {@code @Nullable} values.
 *//*

    @Inject
    LoanRepository(@Remote LoansDataSource LoansRemoteDataSource,
                    @Local LoansDataSource LoansLocalDataSource) {
        mLoansRemoteDataSource = LoansRemoteDataSource;
        mLoansLocalDataSource = LoansLocalDataSource;
    }

    */
/**
 * Gets Loans from cache, local data source (SQLite) or remote data source, whichever is
 * available first.
 * <p>
 * Note: {@link LoadLoansCallback#onDataNotAvailable()} is fired if all data sources fail to get
 * the data.
 *//*

    @Override
    public void getLoans(@NonNull final LoadLoansCallback callback) {
        checkNotNull(callback);

        // Respond immediately with cache if available and not dirty
        if (mCachedLoans != null && !mCacheIsDirty) {
            callback.onLoansLoaded(new ArrayList<>(mCachedLoans.values()));
            return;
        }

        if (mCacheIsDirty) {
            // If the cache is dirty we need to fetch new data from the network.
            getLoansFromRemoteDataSource(callback);
        } else {
            // Query the local storage if available. If not, query the network.
            mLoansLocalDataSource.getLoans(new LoadLoansCallback() {
                @Override
                public void onLoansLoaded(List<Loan> Loans) {
                    refreshCache(Loans);
                    callback.onLoansLoaded(new ArrayList<>(mCachedLoans.values()));
                }

                @Override
                public void onDataNotAvailable() {
                    getLoansFromRemoteDataSource(callback);
                }
            });
        }
    }

    private void getLoansFromRemoteDataSource(@NonNull final LoadLoansCallback callback) {
        mLoansRemoteDataSource.getLoans(new LoadLoansCallback() {
            @Override
            public void onLoansLoaded(List<Loan> Loans) {
                refreshCache(Loans);
                refreshLocalDataSource(Loans);
                callback.onLoansLoaded(new ArrayList<>(mCachedLoans.values()));
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    private void refreshCache(List<Loan> Loans) {
        if (mCachedLoans == null) {
            mCachedLoans = new LinkedHashMap<>();
        }
        mCachedLoans.clear();
        for (Loan Loan : Loans) {
            mCachedLoans.put(Loan.getId(), Loan);
        }
        mCacheIsDirty = false;
    }

    private void refreshLocalDataSource(List<Loan> Loans) {
        mLoansLocalDataSource.deleteAllLoans();
        for (Loan Loan : Loans) {
            mLoansLocalDataSource.saveLoan(Loan);
        }
    }

    @Override
    public void saveLoan(@NonNull Loan Loan) {
        checkNotNull(Loan);
        mLoansRemoteDataSource.saveLoan(Loan);
        mLoansLocalDataSource.saveLoan(Loan);

        // Do in memory cache update to keep the app UI up to date
        if (mCachedLoans == null) {
            mCachedLoans = new LinkedHashMap<>();
        }
        mCachedLoans.put(Loan.getId(), Loan);
    }

    @Override
    public void completeLoan(@NonNull String LoanId) {
        checkNotNull(LoanId);
        completeLoan(getLoanWithId(LoanId));
    }

    @Override
    public void completeLoan(@NonNull Loan Loan) {
        checkNotNull(Loan);
        mLoansRemoteDataSource.completeLoan(Loan);
        mLoansLocalDataSource.completeLoan(Loan);

        Loan completedLoan = new Loan(Loan.getTitle(), Loan.getDescription(), Loan.getId(), true);

        // Do in memory cache update to keep the app UI up to date
        if (mCachedLoans == null) {
            mCachedLoans = new LinkedHashMap<>();
        }
        mCachedLoans.put(Loan.getId(), completedLoan);
    }

    @Nullable
    private Loan getLoanWithId(@NonNull String id) {
        checkNotNull(id);
        if (mCachedLoans == null || mCachedLoans.isEmpty()) {
            return null;
        } else {
            return mCachedLoans.get(id);
        }
    }

    @Override
    public void activateLoan(@NonNull String LoanId) {
        checkNotNull(LoanId);
        activateLoan(getLoanWithId(LoanId));
    }

    @Override
    public void activateLoan(@NonNull Loan Loan) {
        checkNotNull(Loan);
        mLoansRemoteDataSource.activateLoan(Loan);
        mLoansLocalDataSource.activateLoan(Loan);

        Loan activeLoan = new Loan(Loan.getTitle(), Loan.getDescription(), Loan.getId());

        // Do in memory cache update to keep the app UI up to date
        if (mCachedLoans == null) {
            mCachedLoans = new LinkedHashMap<>();
        }
        mCachedLoans.put(Loan.getId(), activeLoan);
    }

    @Override
    public void clearCompletedLoans() {
        mLoansRemoteDataSource.clearCompletedLoans();
        mLoansLocalDataSource.clearCompletedLoans();

        // Do in memory cache update to keep the app UI up to date
        if (mCachedLoans == null) {
            mCachedLoans = new LinkedHashMap<>();
        }
        Iterator<Map.Entry<String, Loan>> it = mCachedLoans.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Loan> entry = it.next();
            if (entry.getValue().isCompleted()) {
                it.remove();
            }
        }
    }

    */
/**
 * Gets Loans from local data source (sqlite) unless the table is new or empty. In that case it
 * uses the network data source. This is done to simplify the sample.
 * <p>
 * Note: {@link GetLoanCallback#onDataNotAvailable()} is fired if both data sources fail to get
 * the data.
 *//*

    @Override
    public void getLoan(@NonNull final String LoanId, @NonNull final GetLoanCallback callback) {
        checkNotNull(LoanId);
        checkNotNull(callback);

        Loan cachedLoan = getLoanWithId(LoanId);

        // Respond immediately with cache if available
        if (cachedLoan != null) {
            callback.onLoanLoaded(cachedLoan);
            return;
        }

        // Load from server/persisted if needed.

        // Is the Loan in the local data source? If not, query the network.
        mLoansLocalDataSource.getLoan(LoanId, new GetLoanCallback() {
            @Override
            public void onLoanLoaded(Loan Loan) {
                // Do in memory cache update to keep the app UI up to date
                if (mCachedLoans == null) {
                    mCachedLoans = new LinkedHashMap<>();
                }
                mCachedLoans.put(Loan.getId(), Loan);
                callback.onLoanLoaded(Loan);
            }

            @Override
            public void onDataNotAvailable() {
                mLoansRemoteDataSource.getLoan(LoanId, new GetLoanCallback() {
                    @Override
                    public void onLoanLoaded(Loan Loan) {
                        // Do in memory cache update to keep the app UI up to date
                        if (mCachedLoans == null) {
                            mCachedLoans = new LinkedHashMap<>();
                        }
                        mCachedLoans.put(Loan.getId(), Loan);
                        callback.onLoanLoaded(Loan);
                    }

                    @Override
                    public void onDataNotAvailable() {
                        callback.onDataNotAvailable();
                    }
                });
            }
        });
    }

    @Override
    public void refreshLoans() {
        mCacheIsDirty = true;
    }

    @Override
    public void deleteAllLoans() {
        mLoansRemoteDataSource.deleteAllLoans();
        mLoansLocalDataSource.deleteAllLoans();

        if (mCachedLoans == null) {
            mCachedLoans = new LinkedHashMap<>();
        }
        mCachedLoans.clear();
    }

    @Override
    public void deleteLoan(@NonNull String LoanId) {
        mLoansRemoteDataSource.deleteLoan(checkNotNull(LoanId));
        mLoansLocalDataSource.deleteLoan(checkNotNull(LoanId));

        mCachedLoans.remove(LoanId);
    }


 */
/*   private WebService webService;

    private LoanCache loanCache;

    @Inject
    public LoanRepository(WebService webservice, LoanDao loanDao, Executor executor) {
        this.webService = webservice;
        this.loanDao = loanDao;
        this.executor = executor;
    }

    public LiveData<Loan> getLoan(int accountNo) {
        final MutableLiveData<Loan> data = new MutableLiveData<>();
        webService.getLoan(accountNo + "").enqueue(new Callback<Loan>() {
            @Override
            public void onResponse(Call<Loan> call, Response<Loan> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<Loan> call, Throwable t) {

            }
        });
        return data;
    }

    public LiveData<List<Loan>> getLoans() {
        final MutableLiveData<List<Loan>> data = new MutableLiveData<>();
        webService.getLoans().enqueue(new Callback<Loan>() {
            @Override
            public void onResponse(Call<Loan> call, Response<Loan> response) {
                data.setValue(Collections.singletonList(response.body()));
            }

            @Override
            public void onFailure(Call<Loan> call, Throwable t) {

            }
        });
        return data;
    }

    public LiveData<User> getUser(String userId) {
        refreshUser(userId);
        // return a LiveData directly from the database.
        return userDao.load(userId);
    }

    private void refreshUser(final String userId) {
        executor.execute(() -> {
            // running in a background thread
            // check if user was fetched recently
            boolean userExists = loanDao.hasUser(FRESH_TIMEOUT);
            if (!userExists) {
                // refresh the data
                Response response = webService.getUser(userId).execute();
                // TODO check for error etc.
                // Update the database.The LiveData will automatically refresh so
                // we don't need to do anything else here besides updating the database
                loanDao.saveLoan(response.body());
            }
        });
    }
*//*


}
*/
