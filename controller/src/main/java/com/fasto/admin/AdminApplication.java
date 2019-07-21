package com.fasto.admin;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 *
 * @author kostenko
 */
@ApplicationPath("/")
public class AdminApplication extends Application {

    public class Headers {
        public static final String X_PAGER_TOTAL_ENTRIES = "X-Pager-Total-Entries";
    }

}
