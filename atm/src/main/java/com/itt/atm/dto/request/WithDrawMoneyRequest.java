package com.itt.atm.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WithDrawMoneyRequest {

    private String atmId;
    private String accountId;
    private String pin;
    private long amount;

}
