package lab5.io;


import lab5.table.Table;

public interface Reader {
    Table readTable(int size);

    int readInt();

    int readIntWithMessage(String message);

    double readDouble();

    double readDoubleWithMessage(String message);

    String readString();

    String readStringWithMessage(String message);

}
