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
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.governance.api.util.BatchResourceBean;
import org.wso2.carbon.governance.api.exception.GovernanceException;
import org.wso2.carbon.governance.registry.extensions.internal.GovernanceRegistryExtensionsComponent;
import org.wso2.carbon.registry.core.Registry;
import org.wso2.carbon.registry.core.exceptions.RegistryException;

import java.util.HashMap;
import java.util.Map;

public class BatchOperation {

    private static final Log log = LogFactory.getLog(BatchOperation.class);
    private Registry registry;

    /**
     * Initializing method for BatchOperation. This creates a Registry instance for the invoke operation
     */
    public BatchOperation(){
        int tenantId = PrivilegedCarbonContext.getCurrentContext().getTenantId();
        String userName = PrivilegedCarbonContext.getCurrentContext().getUsername();
        try{
            this.registry = GovernanceRegistryExtensionsComponent.getRegistryService().getRegistry(userName, tenantId);
        }catch(RegistryException e){
            log.error("Failed to get registry.", e);
        }
    }


    /**
     *
     * @param batchResourceBeans
     * @param parameterMap
     * @return status of the invocation
     * @throws GovernanceException
     */
    public boolean invokeBatchCheckItem(BatchResourceBean[] batchResourceBeans,
                                        Map<String,String> parameterMap)throws GovernanceException{
        String action = "itemClick";
        boolean success = false;

        if (parameterMap == null){
            throw new GovernanceException("parameterMap cannot be null");
        }

        try {
            String aspect = null;
            String[] checkItemsList = null;

            registry.beginTransaction();

            log.info("Transaction began.");

            for (BatchResourceBean batchResourceBean : batchResourceBeans) {
                checkItemsList = batchResourceBean.getCheckListItemsList();
                if (checkItemsList == null || checkItemsList.length != parameterMap.size()) {
                    throw new GovernanceException("Incompatible parameters and checklist items");
                }
                aspect = batchResourceBean.getResourceLCName();

                if (aspect == null) {
                    throw new GovernanceException("Lifecycle not found in resource:"
                            + batchResourceBean.getResourcePath());
                }

                try {

                for (int j = 0; j < parameterMap.size(); j++) {
                    if (!parameterMap.get(j + ".item").equals(checkItemsList[j])) {
                        registry.invokeAspect(batchResourceBean.getResourcePath(), aspect, action, parameterMap);

                    }
                }
                }catch(RegistryException e){
                    String message = "An error occurred while aspect invocation for item check for the resource " +
                            batchResourceBean.getResourcePath();
                    throw new GovernanceException(message, e);
                }

            }
            success = true;
        } catch(GovernanceException e){
            String message = "An error occurred while acquiring data from BatchResourceBean";
            throw new GovernanceException(message, e);
        } catch(RegistryException e){
            String message = "An error occurred while registry transaction process for the batch operation";
            throw new GovernanceException(message, e);
        }finally {
            if(success){
                try{
                    registry.commitTransaction();
                    log.info("Item check successful.");

                } catch(RegistryException e) {
                    throw new GovernanceException(e);
                }
            }
            else try {
                registry.rollbackTransaction();
                log.error("Item check Failed.");
            } catch (RegistryException e) {
                throw new GovernanceException(e);
            }
        }
        return success;

    }

    /**
     *
     * @param batchResourceBeans
     * @param action
     * @return status of the invocation
     * @throws GovernanceException
     */
    public boolean invokeBatchStateTransition(BatchResourceBean[] batchResourceBeans,
                                              String action) throws GovernanceException{
        boolean success = false;

        if (action == null){
            throw new GovernanceException("action cannot be null.");
        }

        try {
            String aspect = null;
            String[] checkItemsList = null;
            Map<String, String> parameterMap = new HashMap<String, String>();

            registry.beginTransaction();

            log.info("Transaction began.");

            for (BatchResourceBean batchResourceBean : batchResourceBeans) {
                checkItemsList = batchResourceBean.getCheckListItemsList();
                if (checkItemsList == null) {
                    throw new GovernanceException("Check items not found in resource:"
                            + batchResourceBean.getResourcePath());
                }

                for (int j = 0; j < checkItemsList.length; j++) {
                    parameterMap.put(j + ".item", checkItemsList[j]);
                }

                aspect = batchResourceBean.getResourceLCName();
                if (aspect == null) {
                    throw new GovernanceException("Lifecycle not found in resource:"
                            + batchResourceBean.getResourcePath());
                }
                try {

                    registry.invokeAspect(batchResourceBean.getResourcePath(), aspect, action, parameterMap);
                } catch (RegistryException e) {
                    String message = "An error occurred while aspect invocation for state transition in the resource " +
                            batchResourceBean.getResourcePath();
                    throw new GovernanceException(message, e);
                }
            }
            success = true;
        } catch(GovernanceException e){
            String message = "An error occurred while acquiring data from BatchResourceBean";
            throw new GovernanceException(message, e);
        } catch(RegistryException e){
            String message = "An error occurred while registry transaction process for the batch operation";
            throw new GovernanceException(message, e);
        }finally {
            if(success){
                try{
                    registry.commitTransaction();
                    log.info("State transition successful.");

                } catch(RegistryException e) {
                    throw new GovernanceException(e);
                }
            }
            else{
                try{
                    registry.rollbackTransaction();
                    log.error("State transition Failed.");
                } catch (RegistryException e){
                    throw new GovernanceException(e);
                }
            }
        }


        return success;
    }

}
