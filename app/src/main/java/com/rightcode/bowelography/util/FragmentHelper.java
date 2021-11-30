package com.rightcode.bowelography.util;


import android.content.Intent;

import androidx.annotation.AnimRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.rightcode.bowelography.R;


public class FragmentHelper {

    public static Fragment findFragmentById(@NonNull FragmentManager fm, int id) {
        return fm.findFragmentById(id);
    }

    public static Fragment findFragmentByTag(@NonNull FragmentManager fm, String tag) {
        return fm.findFragmentByTag(tag);
    }

    public static Fragment getTopFragment(FragmentManager fm) {
        if (fm == null) {
            return null;
        }

        int nSize = fm.getBackStackEntryCount();

        if (nSize <= 0) {
            return null;
        }

        FragmentManager.BackStackEntry tt = fm.getBackStackEntryAt(nSize - 1);
        String str = tt.getName();
        Fragment fragment = fm.findFragmentByTag(str);

        return fragment;

    }

    public static void finishFragment(FragmentManager fm, Fragment fragment) {
        finishFragment(fm, fragment, -1, null);
    }

    public static void finishFragment(FragmentManager fm, Fragment fragment,
                                      int resultCode, Intent data) {
        if (fm == null) {
            return;
        }

        if (fm.getBackStackEntryCount() <= 0) {
            return;
        }

        try {
            fm.popBackStack();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void finishFragment(FragmentManager fm, Class<?> cls) {
        if (fm == null) {
            return;
        }

        int stackPosition = -1;
        int nSize = fm.getBackStackEntryCount();
        for (int i = 0; i < nSize; i++) {
            FragmentManager.BackStackEntry tt = fm.getBackStackEntryAt(i);
            String str = tt.getName();
            Fragment fragment = fm.findFragmentByTag(str);
            if (fragment.getClass() == cls) {
                stackPosition = i;
                break;
            }
        }

        if (stackPosition != -1) {
            for (int i = 0; i < nSize - stackPosition; i++) {
                try {
                    fm.popBackStack();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void startFragment(FragmentManager fm, int containerId,
                                     Fragment fragment) {
        startFragmentForResult(fm, containerId, null, fragment, -1, 0, 0);
    }

    public static void startFragmentWithAnimToLeft(FragmentManager fm, int containerId,
                                                   Fragment fragment) {
        startFragmentForResult(fm, containerId, null, fragment, -1, R.anim.slide_in_activity_from_right, R.anim.slide_out_activity_to_left);
    }

    public static void startFragmentWithAnimToRight(FragmentManager fm, int containerId,
                                                    Fragment fragment) {
        startFragmentForResult(fm, containerId, null, fragment, -1, R.anim.slide_in_activity_from_left, R.anim.slide_out_activity_to_right);
    }

    public static void startFragmentForResult(FragmentManager fm, int containerId, Fragment target,
                                              Fragment fragment, int requestCode,
                                              @AnimRes int enter, @AnimRes int exit) {
        if (fm == null || fragment == null) {
            return;
        }

        FragmentTransaction transaction = fm.beginTransaction();

        if (target != null)
            fragment.setTargetFragment(target, requestCode);

        if (enter > 0 && exit > 0) {
            transaction.setCustomAnimations(enter, exit);
        }

        transaction.replace(containerId, fragment,
                fragment.getClass().getSimpleName());

//        transaction.addToBackStack(fragment.getClass().getSimpleName());
        transaction.commitAllowingStateLoss();
    }

    public static Fragment findFragment(FragmentManager fm, Class<?> cls) {
        if (fm == null || cls == null) {
            return null;
        }

        return findFragmentByTag(fm, cls.getSimpleName());

//        int nSize = fm.getBackStackEntryCount();
//        for (int i = 0; i < nSize; i++) {
//            FragmentManager.BackStackEntry tt = fm.getBackStackEntryAt(i);
//            if (tt == null) {
//                continue;
//            }
//            String str = tt.getName();
//            Fragment fragment = fm.findFragmentByTag(str);
//            if (fragment == null) {
//                continue;
//            }
//            if (fragment.getClass().equals(cls)) {
//                return fragment;
//            }
//        }
//
//        return null;
    }

    public static void showFragment(final FragmentManager fm, Fragment fragment) {
        if (fm == null || fragment == null) {
            return;
        }

        FragmentTransaction transaction = fm.beginTransaction();
        transaction.show(fragment);
        transaction.commitAllowingStateLoss();
    }

    public static void hideFragment(final FragmentManager fm, Fragment fragment) {
        if (fm == null || fragment == null) {
            return;
        }

        FragmentTransaction transaction = fm.beginTransaction();
        transaction.hide(fragment);
        transaction.commitAllowingStateLoss();
    }

    public static void removeFragment(final FragmentManager fm, Fragment fragment) {
        if (fm == null || fragment == null) {
            return;
        }

        if (fragment.getActivity() == null || fragment.getActivity().isFinishing() || fragment.getActivity().isDestroyed()) {
            return;
        }

        FragmentTransaction transaction = fm.beginTransaction();
        transaction.remove(fragment);
        transaction.commitAllowingStateLoss();
    }
}

