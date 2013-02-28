//
//  VINDRank.c
//  VIND
//
//  Created by Wolfert on 1/23/13.
//  Copyright (c) 2013 Lunatech. All rights reserved.
//

#include <sqlite3.h>
#include <jni.h>


void VINDRank(sqlite3_context *context, int argc, sqlite3_value **argv)
{
    if (argc != 1)
        sqlite3_result_null(context);
    if (sqlite3_value_type(argv[0]) == SQLITE_NULL)
        sqlite3_result_null(context);
    
    int weight = 0;
    unsigned int *blob = (unsigned int *)sqlite3_value_blob(argv[0]);
    
    unsigned int numberOfPhrases = blob[0];
    unsigned int numberOfColumns = blob[1];
    
    int phrase;
    
    for (phrase = 0; phrase< numberOfPhrases; ++phrase)
    {
        unsigned int *phraseBlob = &blob[2 + phrase * numberOfColumns * 3];
        
        if (phraseBlob[0]> 0)
            weight += 100;
        if (phraseBlob[3]> 0)
            weight += 60;
        if (phraseBlob[9]> 0)
            weight += 90;
    }
    sqlite3_result_int(context, weight);
}

int attachVINDRank(sqlite3 *db)
{
    return sqlite3_create_function(db, "VINDRank", 1, 1, 0x00, &VINDRank, 0x00, 0x00);
}
