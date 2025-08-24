package com.mentalapp.cbt_basic.viewdata;

import com.mentalapp.common.entity.NegativeFeel;
import com.mentalapp.common.entity.PositiveFeel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CbtBasicsViewData {
    private List<NegativeFeel> negativeFeels;
    private List<PositiveFeel> positiveFeels;
}

