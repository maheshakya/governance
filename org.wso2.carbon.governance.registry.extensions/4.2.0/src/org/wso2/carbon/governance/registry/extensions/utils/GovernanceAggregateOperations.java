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


public class GovernanceAggregateOperations {

    private static final Log log = LogFactory.getLog(GovernanceAggregateOperations.class);
    Registry registry;
    private ServiceTracker serviceTracker;
    private ServiceReference reference;
    private GovernanceBatchValidate governanceBatchValidate;


    public void createBatch(String[] paths, String type){
        BundleContext bundleContext = new GovernanceRegistryExtensionsComponent().getBundleContext();
        log.info("Lifecycle batch creation started.");
        if (bundleContext != null) {
            ServiceTracker tracker =
                    new ServiceTracker(bundleContext, GovernanceBatchValidate.class.getName(),
                            null);
            tracker.open();
            ServiceReference[] references = tracker.getServiceReferences();


            boolean validateStatus = false;
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
    }


}
