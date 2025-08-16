package com.mentalapp.cbt_basic.viewdata;

import com.mentalapp.common.entity.NegativeFeel;
import com.mentalapp.common.entity.PositiveFeel;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CbtBasicsViewData {

    private List<NegativeFeel> negativeFeels;
    private List<PositiveFeel> positiveFeels;

    public CbtBasicsViewData() {
    }

    public CbtBasicsViewData(List<NegativeFeel> negativeFeels, List<PositiveFeel> positiveFeels) {
        this.negativeFeels = negativeFeels;
        this.positiveFeels = positiveFeels;
    }

    public List<NegativeFeel> getNegativeFeels() {
        return negativeFeels;
    }

    public void setNegativeFeels(List<NegativeFeel> negativeFeels) {
        this.negativeFeels = negativeFeels;
    }

    public List<PositiveFeel> getPositiveFeels() {
        return positiveFeels;
    }

    public void setPositiveFeels(List<PositiveFeel> positiveFeels) {
        this.positiveFeels = positiveFeels;
    }
}

