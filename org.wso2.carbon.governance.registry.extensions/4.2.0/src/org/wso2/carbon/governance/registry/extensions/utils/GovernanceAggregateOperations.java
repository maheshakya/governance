package org.wso2.carbon.governance.registry.extensions.utils;

/**
 * Created by maheshakya on 4/10/14.
 */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.wso2.carbon.governance.api.exception.GovernanceException;
import org.wso2.carbon.governance.api.util.GovernanceBatchValidation;
import org.wso2.carbon.governance.api.util.BatchResourceBean;
import org.wso2.carbon.governance.registry.extensions.internal.GovernanceRegistryExtensionsComponent;
import org.wso2.carbon.registry.core.Registry;

import java.util.Map;


public class GovernanceAggregateOperations {

    private static final Log log = LogFactory.getLog(GovernanceAggregateOperations.class);
    Registry registry;
    private ServiceTracker serviceTracker;
    private ServiceReference reference;
    private static GovernanceBatchValidation governanceBatchValidation;


    public static boolean createBatch(BatchResourceBean[] batchResourceBeans , String type){
        boolean validateStatus = false;
        BundleContext bundleContext = new GovernanceRegistryExtensionsComponent().getBundleContext();
        log.info("Lifecycle batch creation started.");
        if (bundleContext != null) {
            ServiceTracker tracker =
                    new ServiceTracker(bundleContext, GovernanceBatchValidation.class.getName(),
                            null);
            tracker.open();
            ServiceReference[] references = tracker.getServiceReferences();



            if (references != null) {
                for (ServiceReference reference : references) {
                    if (type.equals(reference.getProperty("validateMethod"))) {
                        governanceBatchValidation = (GovernanceBatchValidation) tracker.getService(reference);

                        try{
                        validateStatus = governanceBatchValidation.validate(batchResourceBeans);
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

    public boolean bachtCheckItemInvoke(BatchResourceBean[] batchResourceBeans, Map<String,String>
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


}
