package com.katyshevtseva.kikiorgmobile.db;


import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static com.katyshevtseva.kikiorgmobile.db.DbConstants.DATE_FORMAT;

@RequiredArgsConstructor
abstract class AbstractTable<T> {
    @Getter
    private final String name;
    private final AbstractColumn<T> idColumn;
    @Getter
    private final List<AbstractColumn<T>> contentColumns;

    List<AbstractColumn<T>> getAllColumns() {
        List<AbstractColumn<T>> columns = new ArrayList<>(contentColumns);
        columns.add(idColumn);
        return columns;
    }

    abstract T getNewEmptyObject();

    abstract static class AbstractColumn<T> {
        @Getter
        private String name;
        @Getter
        private ColumnDbType dbType;
        @Getter
        private ColumnActualType actualType;

        AbstractColumn(String name, ColumnActualType actualType) {
            this.name = name;
            this.actualType = actualType;
            switch (actualType) {
                case STRING:
                case DATE:
                    dbType = ColumnDbType.STRING;
                    break;
                case LONG:
                case BOOLEAN:
                    dbType = ColumnDbType.LONG;
            }
        }

        abstract Object getActualValue(T t);

        abstract void setActualValue(T t, Object value);

        String getDbValueByActualValue(T t) {
            Object actualValue = getActualValue(t);
            switch (actualType) {
                case STRING:
                case LONG:
                    return "" + actualValue;
                case BOOLEAN:
                    return ((Boolean) actualValue) ? "1" : "0";
                case DATE:
                    return DATE_FORMAT.format(actualValue);
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
            }
            throw new RuntimeException();
        }
    }

    enum ColumnDbType {
        STRING, LONG
    }

    enum ColumnActualType {
        STRING, LONG, DATE, BOOLEAN
    }
}
