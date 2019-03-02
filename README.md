# active-mq-message-store
Inmemory Active MQ implementation in Springboot with inmemory message store as H2

# Getting Started

### Guides
The following guides illustrates how to use certain features concretely:

### Git
* [Source code can be retrieved from public Git with this url]
  (https://github.com/rijuvfrancis/active-mq-message-store.git)
* [Import and Create a workspace for the Project in your IDE]
* [Start application using Sprint Boot App or run the main method]
* [Application  available in localhost and 8006 port(http://localhost:8006)

### Postman Collection
* [Postman Collection is created and exported to Git to invoke each Services,Please import]

### Application Usage
* [Unique session is created which identifies each client ,it has an expiry of ten minutes] 
  (http://localhost:8006/customer/session)
* [Each request must append with session id in request parameter to identify unique client]
* [Each client can send message for create,update ,Delete operations. Application additionally  
  create,update and Delete a Product with id ,description based on the messages received and stores in 
  message store which is an in memory database ]
* [Listing all products , it needs to be in this format]
  (eg : http://localhost:8006/product/list?sessionkey={sessionid})
* [Create and Update methods available in the url]
   (http://localhost:8006/product/sendMessage/create?sessionkey=402D33584F12DDA2AAB06978C5D4D1ED)
   (http://localhost:8006/product/sendMessage/update/1?sessionkey=5F3C7E6576159717556D2D5942074405)
* [Delete method available in the url]
  (http://localhost:8006/product/sendMessage/delete/2?sessionkey=5F3C7E6576159717556D2D5942074405)
* [Logout to Invalidate session and delete all records](http://localhost:8006/customer/logout)

### Documentation
* [Documentation - Swagger documentation is available,Method level comments are added]

### Error handling
* [Error handling is implemented using Controller Advice]

### Other Things used
* [In Memory Database 2 is used as a Message Store]
* [Implementaion done with specific rest api calls using http methods,get,post,put,delete]
* [String method overriden to pass object as JSON]

### Testing
* [Test cases written for the Controller and Main class]

### Hosting
* [Application also hosted in AWS EBS]()
* [Application also hosted in Kubernetes in IBM Cloud]()

### End
