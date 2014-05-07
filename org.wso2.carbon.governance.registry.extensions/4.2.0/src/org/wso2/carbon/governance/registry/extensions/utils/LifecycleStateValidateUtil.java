/*
 * Copyright (c) 2005-2011, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wso2.carbon.governance.registry.extensions.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.governance.api.exception.BatchValidateException;
import org.wso2.carbon.governance.api.util.ArtifactBatchValidator;
import org.wso2.carbon.governance.api.util.BatchResourceBean;

import java.util.Arrays;
import java.util.HashSet;


public class LifecycleStateValidateUtil implements ArtifactBatchValidator {

    private static final Log log = LogFactory.getLog(LifecycleStateValidateUtil.class);

    /**
     *
     * @param batchResourceBeans
     * @return true if all states are same, else false.
     * @throws BatchValidateException
     */
    public boolean validate(BatchResourceBean[] batchResourceBeans) throws BatchValidateException {

        boolean success = false;

        if (batchResourceBeans != null && batchResourceBeans.length > 0){

            String[] currentStates = new String[batchResourceBeans.length];

            for(int i=0; i<batchResourceBeans.length; i++){
                currentStates[i]=batchResourceBeans[i].getResourceCurrentState();
            }

            //Checking whether all elements in the currentStates array are the same
            if( new HashSet<String>(Arrays.asList(currentStates)).size() == 1 ){
                success = true;
            }

        }

        else{
            throw new BatchValidateException("BatchResourceBean array is empty or null");
        }

        return  success;
    }
}
