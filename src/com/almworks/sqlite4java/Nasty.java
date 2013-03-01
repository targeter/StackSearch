package com.almworks.sqlite4java;

public class Nasty {
   public static long getDbPointer(SQLiteConnection connection) {
      return SWIGTYPE_p_sqlite3.getCPtr(connection.connectionHandle());
   }
}
