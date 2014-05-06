package org.wso2.carbon.governance.registry.extensions.utils;

/**
 * Created by maheshakya on 4/17/14.
 */
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.governance.api.exception.GovernanceException;
import org.wso2.carbon.governance.api.util.BatchResourceBean;
import org.wso2.carbon.governance.api.util.GovernanceBatchValidation;

import java.util.Arrays;
import java.util.HashSet;


public class LifecycleStateValidateUtil implements GovernanceBatchValidation{

    private static final Log log = LogFactory.getLog(LifecycleStateValidateUtil.class);

    /**
     *
     * @param batchResourceBeans
     * @return true if all states are same, else false.
     * @throws GovernanceException
     */
    public boolean validate(BatchResourceBean[] batchResourceBeans) throws GovernanceException {

        boolean success = false;

        if (batchResourceBeans != null && batchResourceBeans.length > 0){

            String[] currentStates = new String[batchResourceBeans.length];

            for(int i=0; i<batchResourceBeans.length; i++){
                currentStates[i]=batchResourceBeans[i].getResourceCurrentState();
            }

            if( new HashSet<String>(Arrays.asList(currentStates)).size() == 1 ){
                success = true;
            }

        }

        else{
            throw new GovernanceException("currentStates array is empty or null");
        }

        return  success;
    }
}
