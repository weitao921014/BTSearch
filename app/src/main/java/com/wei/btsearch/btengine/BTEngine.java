package com.wei.btsearch.btengine;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by wei on 17-3-4.
 */
public abstract class BTEngine {

    public abstract ArrayList<BTItem> getItems(String search,int index) throws IOException;
}
