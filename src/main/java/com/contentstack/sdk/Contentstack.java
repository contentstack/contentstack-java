package com.contentstack.sdk;
import com.contentstack.sdk.utility.CSAppConstants;

/**
 * MIT License
 *
 * Copyright (c) 2012 - 2019 Contentstack
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

public class Contentstack {


    private Contentstack(){ }


    /**
     * Authenticates the stack api key of your stack.
     * This must be called before your stack uses Contentstack sdk.
     * <br>
     * You can find your stack api key from web.
     * @param stackApiKey
     * application api Key of your application on Contentstack.
     * @param accessToken
     * access token
     * @param environment
     * environment name
     * @return
     * {@link Stack} instance.
     * @throws Exception
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
                        //Configurator.initialize(new DefaultConfiguration());
                        return initializeStack( stackApiKey, accessToken, config);
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
     * Authenticates the stack api key of your stack.
     * This must be called before your stack uses  Contentstack sdk.
     * <br>
     * You can find your stack api key from web.
     * @param stackApiKey
     * application api Key of your application on  Contentstack.
     * @param accessToken
     * access token
     * @param environment
     * environment name
     * @param config
     * {@link Config} instance to set environment and other configuration details.
     * @return
     * {@link Stack} instance.
     * @throws Exception
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
                        //Configurator.initialize(new DefaultConfiguration());
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
     * This must be called to initialise  Contentstack sdk.
     * <br>
     * You can find your stack api key from web.
     * @param stackApiKey
     * application api Key of your application on  Contentstack.
     * @param accessToken
     * access token
     * @param config
     * {@link Config} instance to set environment and other configuration details.
     * @return
     * {@link Stack} instance.
     * @throws Exception
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
