package com.contentstack.sdk;

import com.contentstack.sdk.Utility.CSAppConstants;

/**
 *  @author  built.io. Inc
 */
public class Contentstack {

    private static final String TAG = "Contentstack";
    private Contentstack(){

    }

    /**
     *
     * Authenticates the stack api key of your stack.
     * This must be called before your stack uses Built.io Contentstack sdk.
     * <br>
     * You can find your stack api key from web.
     *
     * @param stackApiKey
     * application api Key of your application on Built.io Contentstack.
     *
     * @param accessToken
     * access token
     *
     * @param environment
     * environment name
     *
     * @return
     * {@link Stack} instance.
     *
     * @throws Exception
     *
     * <br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     * //'blt5d4sample2633b' is a dummy Stack API key
     * //'blt6d0240b5sample254090d' is dummy access token.
     * Stack stack = Contentstack.stack(context, "blt5d4sample2633b", "blt6d0240b5sample254090d", "stag");
     * </pre>
     */


    public static Stack stack( String stackApiKey, String accessToken, String environment) throws Exception{

            if(!stackApiKey.isEmpty()) {
                if(!accessToken.isEmpty()) {
                    if(!environment.isEmpty()) {
                        Config config = new Config();
                        config.setEnvironment(environment);
                        return initializeStack( stackApiKey, accessToken, config);
                    }else{
                        CSAppConstants.printMessage(CSAppConstants.ErrorMessage_Stack_Environment_IsNull);
                        throw new Exception(CSAppConstants.ErrorMessage_Stack_Environment_IsNull);
                    }
                }else{
                    throw new Exception(CSAppConstants.ErrorMessage_Stack_AccessToken_IsNull);
                }
            }else {
                throw new Exception(CSAppConstants.ErrorMessage_StackApiKeyIsNull);
            }

    }



    /**
     *
     * Authenticates the stack api key of your stack.
     * This must be called before your stack uses Built.io Contentstack sdk.
     * <br>
     * You can find your stack api key from web.
     *
     * @param stackApiKey
     * application api Key of your application on Built.io Contentstack.
     *
     * @param accessToken
     * access token
     *
     * @param environment
     * environment name

     * @param config
     * {@link Config} instance to set environment and other configuration details.
     *
     * @return
     * {@link Stack} instance.
     *
     * @throws Exception
     *
     * <br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     * //'blt5d4sample2633b' is a dummy Stack API key
     * //'blt6d0240b5sample254090d' is dummy access token.
     * Stack stack = Contentstack.stack(context, "blt5d4sample2633b", "blt6d0240b5sample254090d", "stag");
     * </pre>
     */


    public static Stack stack(String stackApiKey, String accessToken, String environment, Config config) throws Exception{

            if(!stackApiKey.isEmpty()) {
                if(!accessToken.isEmpty()) {
                    if(!environment.isEmpty()) {
                        if(config != null){
                            config.setEnvironment(environment);
                        }else {
                            config = new Config();
                            config.setEnvironment(environment);
                        }
                        return initializeStack(stackApiKey, accessToken, config);
                    }else{
                        throw new Exception(CSAppConstants.ErrorMessage_Stack_Environment_IsNull);
                    }
                }else{
                    throw new Exception(CSAppConstants.ErrorMessage_Stack_AccessToken_IsNull);
                }
            }else {
                throw new Exception(CSAppConstants.ErrorMessage_StackApiKeyIsNull);
            }
    }




    /**
     *
     * Initialise the stack api key of your stack.
     * This must be called to initialise Built.io Contentstack sdk.
     * <br>
     * You can find your stack api key from web.
     *
     * @param stackApiKey
     * application api Key of your application on Built.io Contentstack.
     *
     * @param accessToken
     * access token
     *
     * @param config
     * {@link Config} instance to set environment and other configuration details.
     *
     * @return
     * {@link Stack} instance.
     *
     * @throws Exception
     *
     * <br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     * //'blt5d4sample2633b' is a dummy Stack API key
     * //'blt6d0240b5sample254090d' is dummy access token.
     * Stack stack = Contentstack.stack(context, "blt5d4sample2633b", "blt6d0240b5sample254090d", "stag");
     * </pre>
     */

    private static Stack initializeStack(String stackApiKey, String accessToken, Config config){

        Stack stack = new Stack(stackApiKey.trim());
        stack.setHeader("api_key", stackApiKey);
        stack.setHeader("access_token", accessToken);
        stack.setConfig(config);
        return stack;
    }





}
