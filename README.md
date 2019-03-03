# Getting Started

### Guides
The following guides illustrates how to use certain features concretely:

### Git
* [Source code can be retrieved from public Git with this url]
  (https://github.com/rijuvfrancis/active-mq-message-store.git)
* [Import and Create a workspace for the Project in your IDE]
* [Start application using Sprint Boot App or run the main method]
* [Application will be available in localhost and 8006 port(http://localhost:8006)

### Postman Collection
* [Postman Collection is created and exported to Git to invoke each Services,Please import]
  (active-mq-message-store/SpringBoot Active MQ.postman_collection.json)
  
### Application Usage

### Session Management
* [Unique session is created which identifies each client ,it has an expiry of ten minutes] 
  (http://localhost:8006/customer/session)
* [Each request must append with session id in request parameter to identify unique client]
* [Session will automatically expires after 10 minutes as configured value, We can also manually invalidate session by calling Logout function, this will additionally remove all products from DB]

### Rest Api
* [Each client can send message for create,update ,Delete operations ,rest api endpoints are exposed for each services. Application additionally  
  create,update and Delete a Product with id ,description based on the messages received and stores in 
  message store which is an in memory database ]
* [Listing all products , it needs to be in this format]
  (eg : http://localhost:8006/product/list?sessionkey={sessionid})
* [Create and Update methods available in the url]
   (http://localhost:8006/product/sendMessage/create?sessionkey={sessionid})
   (http://localhost:8006/product/sendMessage/update/1?sessionkey={sessionid})
* [Delete method available in the url]
  (http://localhost:8006/product/sendMessage/delete/2?sessionkey={sessionid})
* [Logout to Invalidate session and delete all records](http://localhost:8006/customer/logout)

### Documentation
* [Documentation - Swagger documentation is available,Method level comments are added ,Detailed in Readme files]
  (http://localhost:8006/swagger-ui.html# or http://storytel.us-east-2.elasticbeanstalk.com/swagger-ui.html#)

### Error handling
* [Error handling is implemented using Controller Advice - 404 , Internal Service Error is handled]

### Other Things used
* [In Memory Database H2 is used as a Message Store]
* [Implementaion done with specific rest api calls using http methods,get,post,put,delete]
* [String method overriden to pass object as JSON]

### Testing
* [Test cases written for the Controller and Main class]

### Hosting
* [Application also hosted in AWS EBS](http://storytel.us-east-2.elasticbeanstalk.com/swagger-ui.html#)
* [Application also hosted in Kubernetes in IBM Cloud]()

### End


