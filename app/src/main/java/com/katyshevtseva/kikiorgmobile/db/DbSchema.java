package com.katyshevtseva.kikiorgmobile.db;

class DbSchema {
    static final class IrregularTaskTable {
        static final String NAME = "irregular_task";

        static final class Cols {
            static final String ID = "id";
            static final String TITLE = "title";
            static final String DESC = "desc";
            static final String DATE = "date";
            static final String DONE = "done";
        }
    }
}
