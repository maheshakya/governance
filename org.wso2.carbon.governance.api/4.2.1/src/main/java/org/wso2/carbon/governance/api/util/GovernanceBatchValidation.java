package org.wso2.carbon.governance.api.util;

import org.wso2.carbon.governance.api.exception.GovernanceException;

/**
 * Created by maheshakya on 4/11/14.
 * batch resource array
 */
public interface GovernanceBatchValidation {

    public boolean validate(BatchResourceBean[] batchResourceBeans) throws GovernanceException;
}
