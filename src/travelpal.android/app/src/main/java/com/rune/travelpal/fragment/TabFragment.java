package com.rune.travelpal.fragment;

import rune.fragment.AbstractStatefulFragment;


public abstract class TabFragment<State> extends AbstractStatefulFragment<State> {

    public TabFragment(int layoutId, Class logClass, Class<State> stateClass) {
        super(layoutId, logClass, stateClass);
    }

    public abstract String getTabName();


}
