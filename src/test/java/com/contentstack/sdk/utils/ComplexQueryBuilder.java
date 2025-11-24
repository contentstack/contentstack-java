package com.contentstack.sdk.utils;

import com.contentstack.sdk.Query;
import com.contentstack.sdk.Stack;

/**
 * Builder utility for creating complex queries.
 */
public class ComplexQueryBuilder {
    
    /**
     * Build a simple AND query with two field conditions
     * 
     * @param stack Stack instance
     * @param contentTypeUid Content type UID
     * @param field1 First field name
     * @param field2 Second field name
     * @return Query with AND conditions
     */
    public static Query buildSimpleAndQuery(Stack stack, String contentTypeUid, 
                                           String field1, String field2) {
        Query query = stack.contentType(contentTypeUid).query();
        query.exists(field1);
        query.exists(field2);
        return query;
    }
    
    /**
     * Build a complex AND query with multiple field conditions
     * 
     * @param stack Stack instance
     * @param contentTypeUid Content type UID
     * @param fields Array of field names to check existence
     * @return Query with multiple AND conditions
     */
    public static Query buildMultiFieldAndQuery(Stack stack, String contentTypeUid, 
                                                String... fields) {
        Query query = stack.contentType(contentTypeUid).query();
        for (String field : fields) {
            query.exists(field);
        }
        return query;
    }
    
    /**
     * Build an AND query with field existence and value matching
     * 
     * @param stack Stack instance
     * @param contentTypeUid Content type UID
     * @param existsField Field that must exist
     * @param matchField Field to match value
     * @param matchValue Value to match
     * @return Query with AND conditions
     */
    public static Query buildAndQueryWithValue(Stack stack, String contentTypeUid,
                                              String existsField, String matchField, 
                                              Object matchValue) {
        Query query = stack.contentType(contentTypeUid).query();
        query.exists(existsField);
        query.where(matchField, matchValue);
        return query;
    }
    
    /**
     * Build a query with IN operator for multiple values
     * 
     * @param stack Stack instance
     * @param contentTypeUid Content type UID
     * @param field Field name
     * @param values Array of values
     * @return Query with IN condition
     */
    public static Query buildInQuery(Stack stack, String contentTypeUid,
                                    String field, String[] values) {
        Query query = stack.contentType(contentTypeUid).query();
        query.containedIn(field, values);
        return query;
    }
    
    /**
     * Build a query with NOT IN operator
     * 
     * @param stack Stack instance
     * @param contentTypeUid Content type UID
     * @param field Field name
     * @param excludedValues Array of values to exclude
     * @return Query with NOT IN condition
     */
    public static Query buildNotInQuery(Stack stack, String contentTypeUid,
                                       String field, String[] excludedValues) {
        Query query = stack.contentType(contentTypeUid).query();
        query.notContainedIn(field, excludedValues);
        return query;
    }
    
    /**
     * Build a nested query with AND and OR combinations
     * Example: (field1 exists AND field2 exists) AND (field3 = value)
     * 
     * @param stack Stack instance
     * @param contentTypeUid Content type UID
     * @param existsFields Fields that must exist
     * @param matchField Field to match
     * @param matchValue Value to match
     * @return Nested query
     */
    public static Query buildNestedQuery(Stack stack, String contentTypeUid,
                                        String[] existsFields, String matchField, 
                                        Object matchValue) {
        Query query = stack.contentType(contentTypeUid).query();
        
        // Add existence conditions
        for (String field : existsFields) {
            query.exists(field);
        }
        
        // Add value match condition
        query.where(matchField, matchValue);
        
        return query;
    }
    
    /**
     * Build a query with multiple field filters (multi-field query)
     * 
     * @param stack Stack instance
     * @param contentTypeUid Content type UID
     * @param field1 First field name
     * @param value1 First field value
     * @param field2 Second field name
     * @param value2 Second field value
     * @return Query with multiple field filters
     */
    public static Query buildMultiFieldQuery(Stack stack, String contentTypeUid,
                                            String field1, Object value1,
                                            String field2, Object value2) {
        Query query = stack.contentType(contentTypeUid).query();
        query.where(field1, value1);
        query.where(field2, value2);
        return query;
    }
    
    /**
     * Build a query with pagination
     * 
     * @param stack Stack instance
     * @param contentTypeUid Content type UID
     * @param limit Number of results to return
     * @param skip Number of results to skip
     * @return Query with pagination
     */
    public static Query buildPaginatedQuery(Stack stack, String contentTypeUid,
                                           int limit, int skip) {
        Query query = stack.contentType(contentTypeUid).query();
        query.limit(limit);
        query.skip(skip);
        return query;
    }
    
    /**
     * Build a query with ordering
     * 
     * @param stack Stack instance
     * @param contentTypeUid Content type UID
     * @param orderByField Field to order by
     * @param ascending True for ascending, false for descending
     * @return Query with ordering
     */
    public static Query buildOrderedQuery(Stack stack, String contentTypeUid,
                                         String orderByField, boolean ascending) {
        Query query = stack.contentType(contentTypeUid).query();
        if (ascending) {
            query.ascending(orderByField);
        } else {
            query.descending(orderByField);
        }
        return query;
    }
    
    /**
     * Build a query with pagination and ordering
     * 
     * @param stack Stack instance
     * @param contentTypeUid Content type UID
     * @param limit Number of results
     * @param skip Number to skip
     * @param orderByField Field to order by
     * @param ascending True for ascending order
     * @return Query with pagination and ordering
     */
    public static Query buildPaginatedOrderedQuery(Stack stack, String contentTypeUid,
                                                  int limit, int skip, 
                                                  String orderByField, boolean ascending) {
        Query query = buildPaginatedQuery(stack, contentTypeUid, limit, skip);
        if (ascending) {
            query.ascending(orderByField);
        } else {
            query.descending(orderByField);
        }
        return query;
    }
    
    /**
     * Build a query with single reference inclusion
     * 
     * @param stack Stack instance
     * @param contentTypeUid Content type UID
     * @param referenceField Reference field to include
     * @return Query with reference
     */
    public static Query buildQueryWithReference(Stack stack, String contentTypeUid,
                                               String referenceField) {
        Query query = stack.contentType(contentTypeUid).query();
        query.includeReference(referenceField);
        return query;
    }
    
    /**
     * Build a query with multiple reference inclusions
     * 
     * @param stack Stack instance
     * @param contentTypeUid Content type UID
     * @param referenceFields Array of reference fields
     * @return Query with multiple references
     */
    public static Query buildQueryWithReferences(Stack stack, String contentTypeUid,
                                                String[] referenceFields) {
        Query query = stack.contentType(contentTypeUid).query();
        for (String refField : referenceFields) {
            query.includeReference(refField);
        }
        return query;
    }
    
    /**
     * Build a query with specific fields only
     * 
     * @param stack Stack instance
     * @param contentTypeUid Content type UID
     * @param fields Array of fields to include
     * @return Query with only specified fields
     */
    public static Query buildQueryWithOnlyFields(Stack stack, String contentTypeUid,
                                                String[] fields) {
        Query query = stack.contentType(contentTypeUid).query();
        query.only(fields);
        return query;
    }
    
    /**
     * Build a query excluding specific fields
     * 
     * @param stack Stack instance
     * @param contentTypeUid Content type UID
     * @param fields Array of fields to exclude
     * @return Query excluding specified fields
     */
    public static Query buildQueryExceptFields(Stack stack, String contentTypeUid,
                                              String[] fields) {
        Query query = stack.contentType(contentTypeUid).query();
        query.except(fields);
        return query;
    }
    
    /**
     * Build a comprehensive query with multiple options
     * 
     * @param stack Stack instance
     * @param contentTypeUid Content type UID
     * @param existsFields Fields that must exist
     * @param referenceFields Reference fields to include
     * @param limit Result limit
     * @param orderByField Field to order by
     * @param ascending Order direction
     * @return Complex query with multiple conditions
     */
    public static Query buildComprehensiveQuery(Stack stack, String contentTypeUid,
                                               String[] existsFields, 
                                               String[] referenceFields,
                                               int limit, String orderByField, 
                                               boolean ascending) {
        Query query = stack.contentType(contentTypeUid).query();
        
        // Add existence conditions
        if (existsFields != null) {
            for (String field : existsFields) {
                query.exists(field);
            }
        }
        
        // Add references
        if (referenceFields != null && referenceFields.length > 0) {
            for (String refField : referenceFields) {
                query.includeReference(refField);
            }
        }
        
        // Add pagination
        if (limit > 0) {
            query.limit(limit);
        }
        
        // Add ordering
        if (orderByField != null && !orderByField.isEmpty()) {
            if (ascending) {
                query.ascending(orderByField);
            } else {
                query.descending(orderByField);
            }
        }
        
        return query;
    }
    
    /**
     * Build a query for testing edge cases
     * Returns a query with no conditions (should return all entries)
     * 
     * @param stack Stack instance
     * @param contentTypeUid Content type UID
     * @return Empty query
     */
    public static Query buildEmptyQuery(Stack stack, String contentTypeUid) {
        return stack.contentType(contentTypeUid).query();
    }
    
    /**
     * Build a query that should return no results
     * Uses a non-existent field value
     * 
     * @param stack Stack instance
     * @param contentTypeUid Content type UID
     * @return Query that should return no results
     */
    public static Query buildNoResultsQuery(Stack stack, String contentTypeUid) {
        Query query = stack.contentType(contentTypeUid).query();
        query.where("title", "NonExistentValue_" + System.currentTimeMillis());
        return query;
    }
}

