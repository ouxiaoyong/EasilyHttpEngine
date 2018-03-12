package com.oxy.easilyhttpengine;

/**
 * Created by BelleOU on 18/3/12.
 */

public interface IResponseTransformer {
    String transformResponse(String response) throws Exception;
}
