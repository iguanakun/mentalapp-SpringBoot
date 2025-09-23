package com.mentalapp.cbt_basic.service;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.mentalapp.cbt_basic.util.CbtBasicCommonUtils;
import com.mentalapp.cbt_basic.viewdata.CbtBasicsViewData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CbtBasicIndexServiceTest {
  @Mock private CbtBasicCommonUtils cbtBasicCommonUtils;

  @InjectMocks private CbtBasicsIndexService cbtBasicsIndexService;

  @Test
  void testProcessNew() {
    CbtBasicsViewData expected = new CbtBasicsViewData();
    when(cbtBasicCommonUtils.createAllFeelsViewData()).thenReturn(expected);

    CbtBasicsViewData actual = cbtBasicsIndexService.processNew();

    assertSame(expected, actual);
    verify(cbtBasicCommonUtils).createAllFeelsViewData();
  }
}
