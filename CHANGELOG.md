
## CHANGELOG

## Version 1.4.2
   ###### Date: 23-August-2019
   - [Config] - Added support for ContentstackRegion in Config class
   
------------------------------------------------

## Version 1.4.1
   ###### Date: 21-August-2019
   - [Query] - Added support for whereIn(String key) and whereNotIn(String key) methods
   - [CSAppConstants] - Removed google internet connection check from CSAppConstants
   
------------------------------------------------


## Version 1.4.0
   ###### Date: 26-July-2019
   - [Entry] - Added support for includeReferenceContentTypeUid support in Entry.
   - [Query] - Added support for includeReferenceContentTypeUid support in Query.
   - [Entry] - setLanguage and getLanguage Deprecated in Entry. 
   - [Query] - language deprecated in Query
   - [Entry] - Added method for getLocale and setLocale(String locale) in Entry
   - [Query] - Added method for locale in Query.
   - [Query] - Removed deprecated method for includeSchema in Query
   
------------------------------------------------


## Version 1.3.3
   ###### Date: 21-June-2019
   - Override response hot-fix

------------------------------------------------

## Version 1.3.2
   ### Date: 13-May-2019
   - Removed println
   - Added support for Logger

------------------------------------------------

## Version 1.3.1
  ###### Date: May-02-2019
  - Change: include reference bug fixed
  - added testcase report for v1.3.1


------------------------------------------------


## Version 1.3.0  
  ###### Date: Apr-12-2019  
  Change: Added support of below methods in SDK   
  
```  
 getContentTypes() in Stack class  
 fetch in ContentType class  
```  
  
Below two support from the Config class has been removed permanently    
- public void setSSL(boolean isSSL)setSSL()  
- public boolean isSSL()  
  
------------------------------------------------  

## Version 1.2.1 
###### Date: 14-Mar-2019
- Note: Bug Fixes and code clean up: 
   
--------------------------------
  
Date: 20-Feb-2019   
Maven integration  
Folder structure modified.  
  
------------------------------------------------  
## Version 1.2.0 
###### Date: 15-Dec-2017  
- New Features:
```
Entry- added method ‘addParam’  
Query- added method 'addParam'  
Asset- added method 'addParam'
 ```  
------------------------------------------------  
  
## Version 1.1.0 
###### Date: 10-Nov-2017  
- New Features:

```  
Stack- added method 'ImageTransform'  
Query- added method 'includeContentType'  
QueryResult- added method 'contentType'  
```
------------------------------------------------  
  
#### API deprecation:  
- Query  
- Deprecated method 'includeSchema'  
------------------------------------------------