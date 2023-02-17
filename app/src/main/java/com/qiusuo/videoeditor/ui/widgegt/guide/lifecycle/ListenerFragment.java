package com.qiusuo.videoeditor.ui.widgegt.guide.lifecycle;

import androidx.fragment.app.Fragment;


public class ListenerFragment extends Fragment {

    FragmentLifecycle mFragmentLifecycle;

    public void setFragmentLifecycle(FragmentLifecycle lifecycle) {
        mFragmentLifecycle = lifecycle;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mFragmentLifecycle != null) {
            mFragmentLifecycle.onStart();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mFragmentLifecycle != null) {
            mFragmentLifecycle.onStop();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mFragmentLifecycle != null) {
            mFragmentLifecycle.onDestroyView();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mFragmentLifecycle != null) {
            mFragmentLifecycle.onDestroy();
        }
    }
}
