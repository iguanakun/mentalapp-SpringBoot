package com.mentalapp.cbt_basic.viewdata;

import com.mentalapp.common.entity.NegativeFeel;
import com.mentalapp.common.entity.PositiveFeel;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CbtBasicsViewData {
  private List<NegativeFeel> negativeFeels;
  private List<PositiveFeel> positiveFeels;
}
