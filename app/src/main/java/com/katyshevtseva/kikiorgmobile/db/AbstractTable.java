package com.katyshevtseva.kikiorgmobile.db;


import static com.katyshevtseva.kikiorgmobile.db.DbConstants.DATE_FORMAT;
import static com.katyshevtseva.kikiorgmobile.db.DbConstants.DATE_TIME_FORMAT;

import com.katyshevtseva.kikiorgmobile.utils.OneInOneOutKnob;
import com.katyshevtseva.kikiorgmobile.utils.TwoInKnob;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
abstract class AbstractTable<T> {
    @Getter
    private final String name;
    private final Column<T> idColumn;
    @Getter
    private final List<Column<T>> contentColumns;

    List<Column<T>> getAllColumns() {
        List<Column<T>> columns = new ArrayList<>(contentColumns);
        columns.add(idColumn);
        return columns;
    }

    abstract T getNewEmptyObject();

    @Getter
    static class Column<T> {
        private final String name;
        private ColumnDbType dbType;
        private final ColumnActualType actualType;
        private final OneInOneOutKnob<T, Object> actualValueSupplier;
        private final TwoInKnob<T, Object> actualValueReceiver;

        Column(String name, ColumnActualType actualType,
               OneInOneOutKnob<T, Object> actualValueSupplier,
               TwoInKnob<T, Object> actualValueReceiver) {
            this.name = name;
            this.actualType = actualType;
            this.actualValueSupplier = actualValueSupplier;
            this.actualValueReceiver = actualValueReceiver;
            switch (actualType) {
                case STRING:
                case DATE:
                case DATE_TIME:
                    dbType = ColumnDbType.STRING;
                    break;
                case LONG:
                case BOOLEAN:
                    dbType = ColumnDbType.LONG;
            }
        }

        String getDbValueByActualValue(T t) {
            Object actualValue = actualValueSupplier.execute(t);
            switch (actualType) {
                case STRING:
                case LONG:
                    return "" + actualValue;
                case BOOLEAN:
                    return ((Boolean) actualValue) ? "1" : "0";
                case DATE:
                    return DATE_FORMAT.format(actualValue);
                case DATE_TIME:
                    return DATE_TIME_FORMAT.format(actualValue);
            }
            throw new RuntimeException();
        }

        Object getActualValueByDbValue(Object dbValue) {
            switch (actualType) {
                case LONG:
                case STRING:
                    return dbValue;
                case BOOLEAN:
                    return ((long) dbValue) == 1;
                case DATE:
                    try {
                        return DATE_FORMAT.parse((String) dbValue);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                case DATE_TIME:
                    try {
                        return DATE_TIME_FORMAT.parse((String) dbValue);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
            }
            throw new RuntimeException();
        }
    }

    enum ColumnDbType {
        STRING, LONG
    }

    enum ColumnActualType {
        STRING, LONG, DATE, BOOLEAN, DATE_TIME
    }
}
