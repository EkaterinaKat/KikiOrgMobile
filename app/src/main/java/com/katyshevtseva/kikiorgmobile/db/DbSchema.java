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

    static final class RegularTaskTable {
        static final String NAME = "regular_task";

        static final class Cols {
            static final String ID = "id";
            static final String TITLE = "title";
            static final String DESC = "desc";
            static final String PERIOD_TYPE = "period_type";
            static final String REF_DATE = "ref_date";
            static final String PERIOD = "period";
        }
    }
}
