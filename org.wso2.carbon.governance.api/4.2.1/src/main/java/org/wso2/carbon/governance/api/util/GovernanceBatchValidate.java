package org.wso2.carbon.governance.api.util;

import org.wso2.carbon.governance.api.exception.GovernanceException;

/**
 * Created by maheshakya on 4/11/14.
 * batch resource array
 */
public interface GovernanceBatchValidate {

    public boolean validate(String[] paths) throws GovernanceException;
}
