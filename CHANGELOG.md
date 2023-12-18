# CHANGELOG

## v1.13.0

### Date: 17-Nov-2023

- Updated Latest version of Utils SDK to 1.2.6
- Snyk Issues fixed
- Updated dependencies
- Added support for early access feature

## v1.12.3

### Date: 28-SEP-2023

- Updated Latest version of Utils SDK
- Snyk Issues fixed

## v1.12.2

### Date: 08-AUG-2023

- Updated Utils SDK to v1.2.3

## v1.12.1

### Date: 07-Jun-2023

- Added Support For Nested Assets
- General Code Improvement

## v1.12.0

### Date: 25-APR-2023

- Include metadata support for Asset, Entry and Query,
- Region support for Azure-EU added

## v1.11.0

### Date: 09-FEB-2023

- Addition of Plugin Support,
- #67 error handling infinite loop issue
- Breaking change: Enums PublishType changed from snake-case to ALL_CAPS
- Basic Improvements

## v1.10.1

### Date: 18-June-2022

- Compile Issue With Gradle

### Date: 17-Jun-2022

- #57 CompileJava issue in Gradle build ecosystem (contentstack-utils)
- #58 content_type : Invalid warning for contentTypeUid

---

## v1.10.0

### Date: 03-Jun-2022

- High increase in memory consumption & thread count when updated to the 1.9.0 version
- Setting Proxy in config [#52](https://github.com/contentstack/contentstack-java/issues/52)
- Adding query parameter for the Live Preview
- Enhancement in logger for the different class ( Suggested through a pull
  request ) [#51](https://github.com/contentstack/contentstack-java/pull/51)

---

## v1.9.0

### Date: 16-Mar-2022

- Entry uid bug fixed #45
- Static logger implemented #43
- Scope based dependencies

---

## v1.8.1

### Date: 27-Jan-2022

- Entry uid bug fixed #45
- Static logger implemented #43
- Scope based dependencies

---

## v1.8.0

### Date: 01-Nov-2021

- Added support for live preview
- Added support for branching
- Removed old version of jsoup dependency
- Code improved as per sonar vulnerability guidelines
- Removed deprecated code/Non-working code marked deprecated
- Few breaking changes added.

---

## v1.7.0

### Date: 12-JUL-2021

- Added support for utils function gql.jsonToHtml()
- Bug #32 issue resolved removed e.printStackTrace()
- Instead stacktrace used logger.error()

---

## v1.6.0

### Date: 05-APR-2021

- **Query** : query.includeEmbeddedItems() method support added
- **Entry** : entry.includeEmbeddedItems() method support added

---

## v1.5.7

### Date: 20-Feb-2021

Document updated

---

## v1.5.6

### Date: 27-Jan-2021

Document updated

New Features:
• None

---

## v1.5.5

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

---

## v1.5.4

### Date: 08-DEC-2020

Publish content fallback
New Features:
• [Entry] - Publish fallback method added
• [Query] - Publish fallback method added
• [Asset] - Publish fallback method added, includeDimension method added
• [Assets] - Publish fallback method added

---

## v1.5.3

### Date: 28-July-2020

- **Build Issue** Build update issue fixed

---

## v1.5.2

### Date: 23-July-2020

- **Bump Issue** : Bump Issue: log4j-core from 2.5 to 2.13.2
- **Query** : Query.locale() documentation
- **CSHttpConnection** : StandardCharsets.UTF_8 Support Added

---

## v1.5.1

### Date: 13-Jan-2020

- **Dependency Vulnerability** Java Github reported vulnerable issue on dependency logj

---

## v1.5.0

### Date: 15-Nov-2019

- **Stack**: Added support for function getContentType()
- **ContentType**: updated function fetch()
- **Query**: Updated support of whereIn(String KEY, Query queryObject)
- **Query**: Updated support of whereNotIn(String KEY, Query queryObject)

---

## v1.4.2

### Date: 03-Sept-2019

- **Config** - Added support for Region in Config.

---

## v1.4.1

### Date: 21-August-2019

- **Query** - Added support for whereIn(String key) and whereNotIn(String key) methods
- **CSAppConstants** - Removed google internet connection check from CSAppConstants

---

## v1.4.0

### Date: 26-July-2019

- **Entry** - Added support for includeReferenceContentTypeUid support in Entry.
- **Query** - Added support for includeReferenceContentTypeUid support in Query.
- **Entry** - setLanguage and getLanguage Deprecated in Entry.
- **Query** - language deprecated in Query
- **Entry** - Added method for getLocale and setLocale(String locale) in Entry
- **Query** - Added method for locale in Query.
- **Query** - Removed deprecated method for includeSchema in Query

---

## v1.3.3

### Date: 21-June-2019

- **HOTFIX**: Override response hot-fix

---

## v1.3.2

### Date: 13-May-2019

- **Code Improvement** Removed println
- **Code Improvement** Added support for Logger

---

## v1.3.1

### Date: May-02-2019

- **Change**: include reference bug fixed
- **Added testcase** report for v1.3.1

---

## v1.3.0

### Date: Apr-12-2019

Change: Added support of below methods in SDK

      ```
      getContentTypes() in Stack class
      fetch in ContentType class
      ```

Below two support from the Config class has been removed permanently

- public void setSSL(boolean isSSL)setSSL()
- public boolean isSSL()

---

## v1.2.1

### Date: 14-Mar-2019

- Note: Bug Fixes and code clean up:

Date: 20-Feb-2019
Maven integration  
Folder structure modified.

---

## v1.2.0

### Date: 15-Dec-2017

- New Features:

      ```
      Entry- added method ‘addParam’
      Query- added method 'addParam'
      Asset- added method 'addParam'
      ```

---

## v1.1.0

### Date: 10-Nov-2017

- New Features:

      ```
      Stack- added method 'ImageTransform'
      Query- added method 'includeContentType'
      QueryResult- added method 'contentType'
      ```

---

### API deprecation

- Query
- Deprecated method 'includeSchema'

---
