package com.example.wgu.finance_tracker_backend.models;

import java.math.BigDecimal;

public interface Transactionable {
    void debit(BigDecimal amount);
    void credit(BigDecimal amount);
}
