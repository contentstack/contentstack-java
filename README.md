[![Contentstack](https://www.contentstack.com/docs/static/images/contentstack.png)](https://www.contentstack.com/)

## Java SDK for Contentstack

Contentstack is a headless CMS with an API-first approach. It is a CMS that developers can use to build powerful cross-platform applications in their favorite languages. Build your application frontend, and Contentstack will take care of the rest. [Read More](https://www.contentstack.com/).

Contentstack provides Java SDK to build application on top of Java. Given below is the detailed guide and helpful resources to get started with our Java SDK.


### Prerequisite

You will need JDK installed on your machine. You can install it from [here](http://www.oracle.com/technetwork/java/javase/downloads/jdk9-downloads-3848520.html).

### Setup and Installation

To use the Contentstack Java SDK to your existing project, perform the steps given below:

1. **Maven**
```
<dependency>
  <groupId>com.contentstack.sdk</groupId>
  <artifactId>java</artifactId>
  <version>1.5.0</version>
</dependency>
```

2. **Gradle**
```
implementation 'com.contentstack.sdk:java:1.5.0'
```

### Key Concepts for using Contentstack

#### Stack

A stack is like a container that holds the content of your app. Learn more about [Stacks](https://www.contentstack.com/docs/guide/stack).

#### Content Type

Content type lets you define the structure or blueprint of a page or a section of your digital property. It is a form-like page that gives Content Managers an interface to input and upload content. [Read more](https://www.contentstack.com/docs/guide/content-types).

#### Entry

An entry is the actual piece of content created using one of the defined content types. Learn more about [Entries](https://www.contentstack.com/docs/guide/content-management#working-with-entries).

#### Asset

Assets refer to all the media files (images, videos, PDFs, audio files, and so on) uploaded to Contentstack. These files can be used in multiple entries. Read more about [Assets](https://www.contentstack.com/docs/guide/content-management#working-with-assets).

#### Environment

A publishing environment corresponds to one or more deployment servers or a content delivery destination where the entries need to be published. Learn how to work with [Environments](https://www.contentstack.com/docs/guide/environments).



### Contentstack Java SDK: 5-minute Quickstart

#### Initializing your SDK

To initialize the SDK, specify application  API key, access token, and environment name of the stack as shown in the snippet given below:
```
Stack stack = Contentstack.stack( "apiKey", "accessToken", "environment_name");
```
To get the API credentials mentioned above, log in to your Contentstack account and then in your top panel navigation, go to Settings &gt; Stack to view the API Key and Access Token.



#### Querying content from your stack

To retrieve a single entry from a content type use the code snippet given below:

```
ContentType contentType = stack.contentType("content_type_uid");
Entry blogEntry = contentType.entry("entry_uid");
blogEntry.fetch(new EntryResultCallBack() {
@Override
public void onCompletion(ResponseType responseType, Error error) {
    if (error == null) {
         [Success block]
    } else {
         [Error block]
    }}
 });
  ```
##### Get Multiple Entries

To retrieve multiple entries of a particular content type, use the code snippet given below:

```
//stack is an instance of Stack class
Query blogQuery = stack.contentType("content_type_uid").query();
blogQuery.find(new QueryResultsCallBack() {
@Override
publicvoidonCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
    if(error == null){
       [Success block]
    }else{
       [Error block]
    }}});
  ```


### Advanced Queries

You can query for content types, entries, assets and more using our Java API Reference.

[Java API Reference Doc](https://www.contentstack.com/docs/platforms/java/api-reference/)

### Working with Images

We have introduced Image Delivery APIs that let you retrieve images and then manipulate and optimize them for your digital properties. It lets you perform a host of other actions such as crop, trim, resize, rotate, overlay, and so on.

For example, if you want to crop an image (with width as 300 and height as 400), you simply need to append query parameters at the end of the image URL, such as, https://images.contentstack.io/v3/assets/blteae40eb499811073/bltc5064f36b5855343/59e0c41ac0eddd140d5a8e3e/download?crop=300,400. There are several more parameters that you can use for your images.

[Read Image Delivery API documentation](https://www.contentstack.com/docs/apis/image-delivery-api/).

You can use the Image Delivery API functions in this SDK as well. Here are a few examples of its usage in the SDK.

```
//set the image quality to 100
LinkedHashMap imageParams = new LinkedHashMap();
imageParams.put("quality", 100);
imageUrl = Stack.ImageTransform(imageUrl, imageParams);

//resize the image by specifying width and height
LinkedHashMap imageParams = new LinkedHashMap();
imageParams.put("width", 100);
imageParams.put("height",100);
imageUrl = Stack.ImageTransform(imageUrl, imageParams);

//enable auto optimization for the image
LinkedHashMap imageParams = new LinkedHashMap();
imageParams.put("auto", "webp");
imageUrl = Stack.ImageTransform(imageUrl, imageParams);
```



### Helpful Links

- [Contentstack Website](https://www.contentstack.com)
- [Official Documentation](https://contentstack.com/docs)
- [Content Delivery API Docs](https://contentstack.com/docs/apis/content-delivery-api/)


### The MIT License (MIT)

Copyright © 2012-2019 [Contentstack](https://www.contentstack.com/). All Rights Reserved

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.