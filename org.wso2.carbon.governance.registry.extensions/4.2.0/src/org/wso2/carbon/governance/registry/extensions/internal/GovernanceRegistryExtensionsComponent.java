/*
*  Copyright (c) 2005-2012, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied.  See the License for the
* specific language governing permissions and limitations
* under the License.
*/

package org.wso2.carbon.governance.registry.extensions.internal;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.ComponentContext;
import org.wso2.carbon.core.services.callback.LoginSubscriptionManagerService;
import org.wso2.carbon.governance.api.util.GovernanceBatchValidate;
import org.wso2.carbon.governance.registry.extensions.listeners.RxtLoader;
import org.wso2.carbon.governance.registry.extensions.utils.LifecycleValidateUtil;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.wso2.carbon.registry.core.service.RegistryService;
import org.wso2.carbon.securevault.SecretCallbackHandlerService;
import org.wso2.carbon.registry.core.Registry;
import org.wso2.carbon.registry.core.session.CurrentSession;

import java.util.*;

/**
 * @scr.component name="org.wso2.governance.registry.extensions.services" immediate="true"
 * @scr.reference name="registry.service"
 * interface="org.wso2.carbon.registry.core.service.RegistryService"
 * cardinality="1..1" policy="dynamic"  bind="setRegistryService" unbind="unsetRegistryService"
 * @scr.reference name="login.subscription.service"
 * interface="org.wso2.carbon.core.services.callback.LoginSubscriptionManagerService" cardinality="0..1"
 * policy="dynamic" bind="setLoginSubscriptionManagerService" unbind="unsetLoginSubscriptionManagerService"
 *  @scr.reference name="secret.callback.handler.service"
 * interface="org.wso2.carbon.securevault.SecretCallbackHandlerService"
 * cardinality="1..1"  policy="dynamic"
 * bind="setSecretCallbackHandlerService"
 * unbind="unsetSecretCallbackHandlerService"
 *
 */

public class GovernanceRegistryExtensionsComponent {

    private static final Log log = LogFactory.getLog(GovernanceRegistryExtensionsComponent.class);
    private static RegistryService registryService = null;
    private static SecretCallbackHandlerService secretCallbackHandlerService = null;

    private static Registry registry;
    private static BundleContext bundleContext;
    private static Stack<ServiceRegistration> registrations = new Stack<ServiceRegistration>();

    protected void activate(ComponentContext componentContext) {
       if(log.isDebugEnabled()){
           log.debug("GovernanceRegistryExtensionsComponent activated");
       }

       try{
            bundleContext = componentContext.getBundleContext();
           Dictionary dictionary = new Hashtable();
           dictionary.put("validateMethod", "lifecyleValidation");
            registrations.push(bundleContext.registerService(GovernanceBatchValidate.class.getName(),
                    new LifecycleValidateUtil(), dictionary));
            log.info("Activated Registry core bundle.");
        }catch (Throwable e) {
            log.error("Failed to activate Registry Core bundle ", e);
        }

    }

    protected void setRegistryService(RegistryService registryService) {
        if(registryService!=null && log.isDebugEnabled()){
          log.debug("Registry service initialized");
        }
        this.registryService = registryService;
    }

    protected void unsetRegistryService(RegistryService registryService) {
        this.registryService = null;
    }

    public static RegistryService getRegistryService() throws RegistryException {
        return registryService;
    }

    protected void setLoginSubscriptionManagerService(LoginSubscriptionManagerService loginManager) {
        log.debug("******* LoginSubscriptionManagerServic is set ******* ");
        loginManager.subscribe(new RxtLoader());
    }

    protected void unsetLoginSubscriptionManagerService(LoginSubscriptionManagerService loginManager) {
        log.debug("******* LoginSubscriptionManagerServic is unset ******* ");
    }

    protected void setSecretCallbackHandlerService(SecretCallbackHandlerService secretCallbackHandlerService){
            if (log.isDebugEnabled()) {
                log.debug("Setting SecretCallbackHandlerService");
            }
           this.secretCallbackHandlerService = secretCallbackHandlerService;
    }

    protected void unsetSecretCallbackHandlerService(SecretCallbackHandlerService secretCallbackHandlerService) {
        this.secretCallbackHandlerService = null;
    }

    public static SecretCallbackHandlerService getSecretCallbackHandlerService(){
        return secretCallbackHandlerService;
    }

    protected void deactivate(ComponentContext componentContext) {
        while (!registrations.empty()) {
            registrations.pop().unregister();
        }
        bundleContext = null;
        log.debug("Registry Core bundle is deactivated ");
    }

    public static BundleContext getBundleContext(){

        return bundleContext;
    }
}
