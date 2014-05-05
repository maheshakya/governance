package org.wso2.carbon.governance.registry.extensions.utils;

/**
 * Created by maheshakya on 4/17/14.
 */
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.governance.api.exception.GovernanceException;
import org.wso2.carbon.governance.api.util.GovernanceBatchValidate;
import org.wso2.carbon.registry.core.Registry;
import org.wso2.carbon.registry.core.Resource;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
//import org.wso2.carbon.registry.core.session.CurrentSession;
import org.wso2.carbon.governance.registry.extensions.internal.GovernanceRegistryExtensionsComponent;
import org.wso2.carbon.context.PrivilegedCarbonContext;

public class LifecycleValidateUtil implements GovernanceBatchValidate{

    private static final Log log = LogFactory.getLog(LifecycleValidateUtil.class);
    private Registry registry;
    private Resource resource;
    private boolean success;

    private final String REGISTRY_LC_NAME = "registry.LC.name";
    private final String REGISTRY_LIFECYCLE = "registry.lifecycle.";


    public LifecycleValidateUtil(){
        this.success = false;
    }

    /**
     *
     * @param paths
     * @return
     * @throws GovernanceException
     */
    public boolean validate(String[] paths) throws GovernanceException {

        int tenantId = PrivilegedCarbonContext.getCurrentContext().getTenantId();
        String userName = PrivilegedCarbonContext.getCurrentContext().getUsername();
        try{
            this.registry = GovernanceRegistryExtensionsComponent.getRegistryService().getRegistry(userName, tenantId);
        }catch(RegistryException e){
            log.error("Failed to get registry.", e);
            throw new GovernanceException("Failed to get registry",e);
            //todo : throw exception
        }

        if (paths != null && paths.length > 0){
            try {
                resource = registry.get(paths[0]);
            } catch (RegistryException e){
                String message = "Failed to get resource from path:" + paths[0];
                log.error(message, e);
                throw new GovernanceException(message, e);
            }

            String currentState;
            String stateProperty;
            String lifecycleName;

            lifecycleName = resource.getProperty(REGISTRY_LC_NAME);

            if (lifecycleName==null){
                throw new GovernanceException("Failed to get lifecycle name from " + paths[0]);
            }

            stateProperty = REGISTRY_LIFECYCLE + lifecycleName + ".state";
            currentState = resource.getProperty(stateProperty);

            if(currentState==null){
                throw new GovernanceException("Failed to get state from " + paths[0]);
            }

            String state;
            for (int i = 1; i < paths.length; i++){
                try {
                    resource = registry.get(paths[i]);
                } catch (RegistryException e){
                    String message = "Failed to get resource from path:" + paths[i];
                    log.error(message);
                    throw new GovernanceException(message, e);

                }
                lifecycleName = resource.getProperty(REGISTRY_LC_NAME);

                if (lifecycleName==null){
                    throw new GovernanceException("Failed to get lifecycle name from " + paths[i]);
                }

                stateProperty = REGISTRY_LIFECYCLE + lifecycleName + ".state";

                state = resource.getProperty(stateProperty);

                if(state==null){
                    throw new GovernanceException("Failed to get state from " + paths[i]);
                }

                if (!state.equals(currentState) ){
                    String message = "States do not match. States: " + currentState + " and " + state;
                    log.error(message);
                    success = false;
                    break;

                }
                else{
                    success = true;
                }

            }

        }

        else{
            throw new GovernanceException("paths array is empty or null");
        }

        return  success;
    }
}
