package com.example.todoNew;

public class DataSourceFactory {
    public static DataSource createDataSource(boolean useH2) {
        if (useH2) {
            return new H2DataSource();
        } else {
            return new FileDataSource();
        }
    }
}
