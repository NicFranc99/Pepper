package com.example.pepperapp28aprile;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

public class PageViewModel extends ViewModel {

    private MutableLiveData<Integer> mIndex = new MutableLiveData<>();
    private String currentpage = "1";
    private LiveData<String> mText = Transformations.map(mIndex, new Function<Integer, String>() {

        @Override
        public String apply(Integer input) {
            currentpage = input.toString();
            return currentpage;
        }
    });

    public void setIndex(int index) {
        mIndex.setValue(index);
    }

    public Integer getTab()
    {
        return Integer.parseInt(currentpage);
    }

    public LiveData<String> getText() {
        return mText;
    }
}