package com.Query.Columnar.Store;

import com.Query.Columnar.Index.SubColumnTimes;
import com.github.luben.zstd.ZstdCompressCtx;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class ToIndex
{
    private static final ZstdCompressCtx compress = new ZstdCompressCtx();

    public static void toLocalTimeIndex(Map<Object, SubColumnTimes.TimeKValue<Object, Object>> subColTimeObject) throws IOException {
        /**
         * flag tolocal index file name in this :)
         */
        final String timeIndexName = "SubColumnIndex.json";

        new ObjectMapper().writeValue(new File("./" + timeIndexName), subColTimeObject);
    }

    public static Map<Object, SubColumnTimes.TimeKValue<Object, Object>> loadLocalTimeIndex() {
        
    }
}
