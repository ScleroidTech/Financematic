/*
 *  Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      https://mindorks.com/license/apache-v2
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License
 */

package com.scleroid.financematic.base;

import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.support.AndroidSupportInjection;

/**
 * Base class for all fragments
 *
 * @param <V> generic viewmodel object
 */

public abstract class BaseFragment<V extends BaseViewModel> extends Fragment {

	/**
	 * Viewmodel Factory object, injected via DI
	 */
	@Inject
	protected ViewModelProvider.Factory viewModelFactory;
	/**
	 * Calling activity object
	 */
	@Nullable
	private BaseActivity mActivity;
	/**
	 * Unbinder for
	 *
	 * @see ButterKnife
	 */
	private Unbinder unbinder;
	/**
	 * Rootview displaying the current fragment
	 */
	private View rootView;

	/**
	 * Attaches itself to activity if the context is part of
	 *
	 * @param context context of calling activity
	 * @see BaseActivity
	 */
	@Override
	public void onAttach(Context context) {
		super.onAttach(context);

		if (context instanceof BaseActivity) {
			BaseActivity activity = (BaseActivity) context;
			this.mActivity = activity;
		}
	}

	/**
	 * Creates and initializes the
	 *
	 * @param savedInstanceState bundle object for initialization parameter
	 * @see Fragment here
	 */
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		performDependencyInjection();
		super.onCreate(savedInstanceState);


		final V mViewModel = getViewModel();
		setHasOptionsMenu(false);
	}

	/**
	 * Called to have the fragment instantiate its user interface view. This is optional, and
	 * non-graphical fragments can return null (which is the default implementation).  This will be
	 * called between {@link #onCreate(Bundle)} and {@link #onActivityCreated(Bundle)}.
	 * <p>
	 * <p>If you return a View from here, you will later be called in {@link #onDestroyView} when
	 * the view is being released.
	 *
	 * @param inflater           The LayoutInflater object that can be used to inflate any views in
	 *                           the fragment,
	 * @param container          If non-null, this is the parent view that the fragment's UI should
	 *                           be attached to.  The fragment should not add the view itself, but
	 *                           this can be used to generate the LayoutParams of the view.
	 * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous
	 *                           saved state as given here.
	 * @return Return the View for the fragment's UI, or null.
	 */
	@Nullable
	@Override
	public View onCreateView(@NonNull final LayoutInflater inflater,
	                         @Nullable final ViewGroup container,
	                         @Nullable final Bundle savedInstanceState) {
		rootView = inflater.inflate(getLayoutId(), container, false);
		unbinder = ButterKnife.bind(this, getRootView());

		return rootView;
	}

	/**
	 * @return layout resource id
	 */
	public abstract
	@LayoutRes
	int getLayoutId();

	/**
	 * @return Root View
	 */
	public View getRootView() {
		return rootView;

	}

	/**
	 * Called when the fragment's activity has been created and this fragment's view hierarchy
	 * instantiated.  It can be used to do final initialization once these pieces are in place,
	 * such
	 * as retrieving views or restoring state.  It is also useful for fragments that use {@link
	 * #setRetainInstance(boolean)} to retain their instance, as this callback tells the fragment
	 * when it is fully associated with the new activity instance.  This is called after {@link
	 * #onCreateView} and before {@link #onViewStateRestored(Bundle)}.
	 *
	 * @param savedInstanceState If the fragment is being re-created from a previous saved state,
	 *                           this is the state.
	 */
	@Override
	public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		subscribeToLiveData();
	}

	/**
	 * Called when the view previously created by {@link #onCreateView} has been detached from the
	 * fragment.  The next time the fragment needs to be displayed, a new view will be created.
	 * This
	 * is called after {@link #onStop()} and before {@link #onDestroy()}.  It is called
	 * <em>regardless</em> of whether {@link #onCreateView} returned a non-null view. Internally it
	 * is called after the view's state has been saved but before it has been removed from its
	 * parent.
	 */
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		unbinder.unbind();
	}

	/**
	 * Detaches the view from the activity
	 */
	@Override
	public void onDetach() {
		mActivity = null;
		super.onDetach();
	}

	/**
	 * Override so you can observe your viewModel
	 */
	protected abstract void subscribeToLiveData();

	private void performDependencyInjection() {
		AndroidSupportInjection.inject(this);
	}

	/**
	 * Override for set view model
	 *
	 * @return view model instance
	 */
	public abstract V getViewModel();

	@Nullable
	public BaseActivity getBaseActivity() {
		return mActivity;
	}

}
