### Tips
#### src/main/java/com/yahoo/graphql/sample/GraphQLEndpoint.java
  - Override createContext
  - read http header to get Authorization
  - parse Authorization to get userId
  - get user data by userId
  - if not valid user return null ( you can file a GraphQLException )
  
#### src/main/webapp/index.html
  - add 'Authorization': 'Bearer 59e08d6393f16c5eea9fa3cd' into header on Function graphQLFetcher

#### TEST script:
curl "http://rocklock.corp.gq1.yahoo.com:8080/graphql?query=%7B%0A%20%20allLinks%20%7B%0A%20%20%20%20url%0A%20%20%7D%0A%7D"

curl -H "Authorization: Bearer 5a644bca93f16c43c58d1fff" "http://rocklock.corp.gq1.yahoo.com:8080/graphql?query=%7B%0A%20%20allLinks%20%7B%0A%20%20%20%20url%0A%20%20%7D%0A%7D"

