# CHANGELOG


## Version 1.8.1

### Date: 21-Jan-2022

- Entry uid bug fixed #45
- Static logger implemented #43
- Scope based dependencies

------------------------------------------------

## Version 1.8.0

### Date: 01-Nov-2021

- Added support for live preview
- Added support for branching
- Removed old version of jsoup dependency
- Code improved as per sonar vulnerability guidelines
- Removed deprecated code/Non-working code marked deprecated
- Few breaking changes added.

------------------------------------------------

## Version 1.7.0

### Date: 12-JUL-2021

- Added support for utils function gql.jsonToHtml()
- Bug #32 issue resolved removed e.printStackTrace()
- Instead stacktrace used logger.error()

------------------------------------------------

## Version 1.6.0

### Date: 05-APR-2021

- **Query** : query.includeEmbeddedItems() method support added
- **Entry** :  entry.includeEmbeddedItems() method support added

------------------------------------------------

## Version 1.5.7

### Date: 20-Feb-2021

Document updated

------------------------------------------------

## Version 1.5.6

### Date: 27-Jan-2021

Document updated

New Features:
• None

------------------------------------------------

## Version 1.5.5

### Date: 22-Jan-2021

Bug fix: Error contains information like

- Error message
- Error code
- Error Details

Language Enum:

- GERMEN changed to GERMAN
- Deprecated Language Enum
- Deprecated LanguageCode Enum

New Features:
• None

------------------------------------------------

## Version 1.5.4

### Date: 08-DEC-2020

Publish content fallback
New Features:
• [Entry] - Publish fallback method added
• [Query] - Publish fallback method added
• [Asset] - Publish fallback method added, includeDimension method added
• [Assets] - Publish fallback method added

------------------------------------------------

## Version 1.5.3

### Date: 28-July-2020

- **Build Issue**   Build update issue fixed

------------------------------------------------

## Version 1.5.2

### Date: 23-July-2020

- **Bump Issue** : Bump Issue: log4j-core from 2.5 to 2.13.2
- **Query** : Query.locale() documentation
- **CSHttpConnection** : StandardCharsets.UTF_8 Support Added

------------------------------------------------

## Version 1.5.1

### Date: 13-Jan-2020

- **Dependency Vulnerability**  Java Github reported vulnerable issue on dependency logj

------------------------------------------------

## Version 1.5.0

### Date: 15-Nov-2019

- **Stack**: Added support for function getContentType()
- **ContentType**: updated function fetch()
- **Query**: Updated support of whereIn(String KEY, Query queryObject)
- **Query**: Updated support of whereNotIn(String KEY, Query queryObject)

------------------------------------------------

## Version 1.4.2

### Date: 03-Sept-2019

- **Config** - Added support for Region in Config.

------------------------------------------------

## Version 1.4.1

### Date: 21-August-2019

- **Query** - Added support for whereIn(String key) and whereNotIn(String key) methods
- **CSAppConstants** - Removed google internet connection check from CSAppConstants

------------------------------------------------

## Version 1.4.0

### Date: 26-July-2019

- **Entry** - Added support for includeReferenceContentTypeUid support in Entry.
- **Query** - Added support for includeReferenceContentTypeUid support in Query.
- **Entry** - setLanguage and getLanguage Deprecated in Entry.
- **Query** - language deprecated in Query
- **Entry** - Added method for getLocale and setLocale(String locale) in Entry
- **Query** - Added method for locale in Query.
- **Query** - Removed deprecated method for includeSchema in Query

------------------------------------------------

## Version 1.3.3

### Date: 21-June-2019

- **HOTFIX**: Override response hot-fix

------------------------------------------------

## Version 1.3.2

### Date: 13-May-2019

- **Code Improvement** Removed println
- **Code Improvement** Added support for Logger

------------------------------------------------

## Version 1.3.1

### Date: May-02-2019

- **Change**: include reference bug fixed
- **Added testcase** report for v1.3.1

------------------------------------------------

## Version 1.3.0  

### Date: Apr-12-2019  

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

### Date: 14-Mar-2019

- Note: Bug Fixes and code clean up:
  
Date: 20-Feb-2019
Maven integration  
Folder structure modified.  
  
------------------------------------------------  

## Version 1.2.0

### Date: 15-Dec-2017  

- New Features:

      ```
      Entry- added method ‘addParam’  
      Query- added method 'addParam'  
      Asset- added method 'addParam'
      ```  

------------------------------------------------  
  
## Version 1.1.0

### Date: 10-Nov-2017  

- New Features:

      ```  
      Stack- added method 'ImageTransform'  
      Query- added method 'includeContentType'  
      QueryResult- added method 'contentType'  
      ```

------------------------------------------------  
  
### API deprecation  

- Query  
- Deprecated method 'includeSchema'  

------------------------------------------------
