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
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.wso2.carbon.governance.api.exception.GovernanceException;
import org.wso2.carbon.governance.api.util.ArtifactBatchValidator;
import org.wso2.carbon.governance.api.util.BatchResourceBean;
import org.wso2.carbon.governance.registry.extensions.internal.GovernanceRegistryExtensionsComponent;
import org.wso2.carbon.registry.core.Registry;

import java.util.Map;

/**
 * This class contains the overall set of operations for batch of resources
 */
public class GovernanceAggregateOperations {

    private static final Log log = LogFactory.getLog(GovernanceAggregateOperations.class);
    private static ArtifactBatchValidator artifactBatchValidator;


    /**
     * Validates the batch of resources in the BatchResourceBean array according to the validator specified.
     * @param batchResourceBeans
     * @param type : type of the validateMethod
     * @return result of the validation
     */
    public static boolean createBatch(BatchResourceBean[] batchResourceBeans , String type){
        boolean validateStatus = false;
        BundleContext bundleContext = new GovernanceRegistryExtensionsComponent().getBundleContext();
        log.info("Lifecycle batch creation started.");
        if (bundleContext != null) {
            ServiceTracker tracker =
                    new ServiceTracker(bundleContext, ArtifactBatchValidator.class.getName(),
                            null);
            tracker.open();
            ServiceReference[] references = tracker.getServiceReferences();


            if (references != null) {
                for (ServiceReference reference : references) {
                    if (type.equals(reference.getProperty("validateMethod"))) {
                        artifactBatchValidator = (ArtifactBatchValidator) tracker.getService(reference);

                        try{
                        validateStatus = artifactBatchValidator.validate(batchResourceBeans);
                        }catch (GovernanceException e)
                        {
                            log.error("Batch Validation failed" ,e);
                        }

                        break;
                    }
                    else{
                        log.info(reference.getProperty("validateMethod") + " doesn't match with " + type);
                    }
                }
            }
            log.info("validate success: " + validateStatus);
            tracker.close();
        }
        else{
            log.info("Batch creation failed. BundleContext is null.");
        }

        return validateStatus;
    }

    /**
     * Validates the batch. If successful, proceeds with the check items checking for the batch
     * @param batchResourceBeans
     * @param parameterMap
     * @param batchValidateMethod
     * @return result of the check item invocation
     */
    public boolean batchCheckItemInvoke(BatchResourceBean[] batchResourceBeans, Map<String,String>
            parameterMap, String batchValidateMethod){
        boolean success = false;
        boolean proceed = GovernanceAggregateOperations.createBatch(batchResourceBeans, batchValidateMethod);

        if(proceed){
            BatchOperation batchCheckItem = new BatchOperation();
            try {
                success = batchCheckItem.invokeBatchCheckItem(batchResourceBeans, parameterMap);
            }catch(GovernanceException e){
                log.error("CheckItem invokation for batch failed.", e);
            }

        }
        return success;
    }

    /**
     * Validates the batch. If successful, proceeds with the state transition for the batch
     * @param batchResourceBeans
     * @param action
     * @param batchValidateMethod
     * @return result of the state transition invocation
     */
    public boolean batchStateTransitionInvoke(BatchResourceBean[] batchResourceBeans,
                                              String action, String batchValidateMethod){
        boolean success = false;
        boolean proceed = GovernanceAggregateOperations.createBatch(batchResourceBeans, batchValidateMethod);

        if(proceed){
            BatchOperation batchStateTransition = new BatchOperation();
            try {
                success = batchStateTransition.invokeBatchStateTransition(batchResourceBeans, action);
            }catch(GovernanceException e){
                log.error("State transition invokation for batch failed.", e);
            }

        }

        return success;
    }

    /**
     * This method can be used to get the registered custom validators
     * @return an array of available validator names
     */
    public static String[] getRegisteredValidators(){
        String[] validators = null;

        BundleContext bundleContext = new GovernanceRegistryExtensionsComponent().getBundleContext();

        if (bundleContext != null) {
            ServiceTracker tracker =
                    new ServiceTracker(bundleContext, ArtifactBatchValidator.class.getName(),
                            null);
            tracker.open();
            ServiceReference[] references = tracker.getServiceReferences();

            if (references != null) {
                validators = new String[references.length];

                for (int i=0; i<references.length; i++) {
                    validators[i] = references[i].getProperty("validateMethod").toString();
                }
            }
            tracker.close();
        }
        else{
            log.error("Unable to retrieve validators");
        }
        return validators;
    }


}
