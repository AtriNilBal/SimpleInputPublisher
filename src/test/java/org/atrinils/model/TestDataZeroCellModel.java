package org.atrinils.model;

import com.creditdatamw.zerocell.annotation.Column;
import com.creditdatamw.zerocell.annotation.RowNumber;
import org.atrinils.converters.StringToCurrencyConverter;
import org.atrinils.enums.Currency;

public class TestDataZeroCellModel {

    @RowNumber
    private int rowNumber;
    @Column(index=0, name="TestcaseId")
    private int testCaseId;

    @Column(index=1, name="Currency", converterClass = StringToCurrencyConverter.class)
    private Currency currency;

    @Column(index=2, name="Name")
    private String name;

    @Column(index=3, name="Execute")
    private String execute;

    @Override
    public String toString() {
        return "TestDataZeroCellModel{" +
                "rowNumber=" + rowNumber +
                ", testCaseId=" + testCaseId +
                ", currency='" + currency + '\'' +
                ", name='" + name + '\'' +
                ", execute=" + execute +
                '}';
    }
}
