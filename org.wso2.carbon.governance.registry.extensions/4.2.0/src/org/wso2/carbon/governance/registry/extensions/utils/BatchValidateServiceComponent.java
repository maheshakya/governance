package org.wso2.carbon.governance.registry.extensions.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.ComponentContext;
import org.wso2.carbon.governance.api.util.GovernanceBatchValidate;
import org.wso2.carbon.registry.core.Registry;
import org.wso2.carbon.registry.core.service.RegistryService;
//import org.wso2.carbon.registry.extensions.utils.LifecycleValidateUtil;

import java.util.Stack;

/**
 * Created by maheshakya on 4/21/14.
 */
public class BatchValidateServiceComponent{

    private static final Log log = LogFactory.getLog(BatchValidateServiceComponent.class);
    private static Registry registry ;

    private static BundleContext bundleContext;
    private static Stack<ServiceRegistration> registrations = new Stack<ServiceRegistration>();
    private static RegistryService registryService;

    /**
     *
     * @param registry
     */
    public BatchValidateServiceComponent(Registry registry){
        this.registry = registry;
    }

    /**
     *
     * @param context
     */
    protected void activate(ComponentContext context){
        bundleContext = context.getBundleContext();
        try{
            registrations.push(bundleContext.registerService(GovernanceBatchValidate.class.getName(),
                    new LifecycleValidateUtil(this.registry), null));
            log.info("Activated Registry core bundle.");
        }catch (Throwable e) {
            log.error("Failed to activate Registry Core bundle ", e);
        }
    }

    protected void deactivate(ComponentContext context) {
        while (!registrations.empty()) {
            registrations.pop().unregister();
        }
        bundleContext = null;
        log.debug("Registry Core bundle is deactivated ");
    }

    /**
     *
     * @return bundleContext
     */
    public static BundleContext getBundleContext(){


        return bundleContext;
    }
}
