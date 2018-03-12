package com.oxy.easilyhttpengine;

import java.util.Map;

/**
 * Created by BelleOU on 18/3/12.
 */

public interface IParamsTransformer {
    Map<String,String> transformParams(Map<String,String> params) throws Exception;
}
