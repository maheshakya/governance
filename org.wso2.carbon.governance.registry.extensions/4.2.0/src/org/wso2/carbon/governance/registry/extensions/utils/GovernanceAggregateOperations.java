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
import org.wso2.carbon.governance.api.util.GovernanceBatchValidate;
import org.wso2.carbon.governance.registry.extensions.internal.GovernanceRegistryExtensionsComponent;
import org.wso2.carbon.registry.core.Registry;

import java.util.Map;


public class GovernanceAggregateOperations {

    private static final Log log = LogFactory.getLog(GovernanceAggregateOperations.class);
    Registry registry;
    private ServiceTracker serviceTracker;
    private ServiceReference reference;
    private static GovernanceBatchValidate governanceBatchValidate;


    public static boolean createBatch(String[] paths, String type){
        boolean validateStatus = false;
        BundleContext bundleContext = new GovernanceRegistryExtensionsComponent().getBundleContext();
        log.info("Lifecycle batch creation started.");
        if (bundleContext != null) {
            ServiceTracker tracker =
                    new ServiceTracker(bundleContext, GovernanceBatchValidate.class.getName(),
                            null);
            tracker.open();
            ServiceReference[] references = tracker.getServiceReferences();



            if (references != null) {
                for (ServiceReference reference : references) {
                    if (type.equals(reference.getProperty("validateMethod"))) {
                        governanceBatchValidate = (GovernanceBatchValidate) tracker.getService(reference);

                        try{
                        validateStatus = governanceBatchValidate.validate(paths);
                        }catch (GovernanceException e)
                        {
                            log.error("Batch Validation failed" ,e);
                        }

                        break;
                    }
                    else{
                        log.info(reference.getProperty("type") + " doesn't match with " + type);
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

    public boolean bachtCheckItemInvoke(String paths[], Map<String,String>
            parameterMap, String batchValidateMethod){
        boolean success = false;
        boolean proceed = GovernanceAggregateOperations.createBatch(paths, batchValidateMethod);

        if(proceed){
            BatchOperation batchCheckItem = new BatchOperation();
            try {
                success = batchCheckItem.invokeBatchCheckItem(paths, parameterMap);
            }catch(GovernanceException e){
                log.error("CheckItem invokation for batch failed.", e);
            }

        }
        return success;
    }

    public boolean batchStateTransitionInvoke(String paths[], String action, String batchValidateMethod){
        boolean success = false;
        boolean proceed = GovernanceAggregateOperations.createBatch(paths, batchValidateMethod);

        if(proceed){
            BatchOperation batchCheckItem = new BatchOperation();
            try {
                success = batchCheckItem.invokeBatchStateTransition(paths, action);
            }catch(GovernanceException e){
                log.error("State transition invokation for batch failed.", e);
            }

        }

        return success;
    }


}
