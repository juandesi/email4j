# Email4J [![Build Status](https://travis-ci.org/juandesi/email4j.svg?branch=develop)](https://travis-ci.org/juandesi/email4j)


Email4J (Email for Java) is a high level java library built on top of the javax.mail api for managing and sending emails, without the necessity of knowing 
any specifications of the underlying transports.

There are several entities that wrap the low level objects that the transport handles to give the developer a friendly and easy way to deal with mailboxes.

## How to get it

You can include this library in your project using maven by adding the artifact and repository as follows.

```xml
<groupId>desi.juan</groupId>
<artifactId>email4j</artifactId>
<version>1.0-SNAPSHOT</version>
```

```xml
<repository>
    <id>email4j-repo</id>
    <url>https://raw.github.com/juandesi/email4j/mvn-repo/</url>
</repository>
```
    
NOTE: this is a custom repository deployed into a github branch.   
 
## Introduction

Basically you have 3 different clients for connecting with mailboxes.

* **a SmtpClient**
* **a Pop3Client**
* **an ImapClient**

Each client has a specific set of operations that work for the underlying used protocol, some clients may share operations. i.e: the Pop3Client and the ImapClient
share the retrieve operation (with differences but they both retrieve emails).

### The SmtpClient

This client is mainly used to send emails, it's composed by only one operation `send` that receives an email and sends it to the specified recipients.

```java
SmtpClient client = new SmtpClient("juan", "desimoni", "juan.smtp.host", SmtpClient.DEFAULT_SMTP_PORT, new ClientConfiguration());
client.send(email) // pre-built outgoing email.
```

To build outgoing emails Email4J provides a simple builder, the EmailBuilder.

```java
EmailBuilder.newEmail()
        .withSubject("This is an email subject")
        .to(singletonList("juandesimoni@gmail.com"))
        .withBody("Body of the message")
        .addAttachment(new EmailAttachment("id", "AttachmentContent", "text/plain; charset=UTF-8"))
        .build();
```

Of course there are other options for more complex body objects and attachments.

### The ImapClient
The ImapClient talks directly to a mailbox where you can retrieve, move, mark or delete emails.

```java
ImapClient client = new ImapClient("juan", "desimoni", "juan.imap.host", ImapClient.DEFAULT_IMAPS_PORT, new ClientConfiguration());
```

The main ImapClient operation is the retrieve operation, you only need to specify a folder and the client will retrieve all the emails 
contained in it. Also a boolean value is required for this operation since the IMAP protocol provides the capability to fetch emails 
without opening their content, this means not marking the email as SEEN email, but no body and attachments will be available in the resultant
emails.

```java
imapClient.retrieve(EmailConstants.INBOX_FOLDER, true);
```

### The Pop3Client 
The Pop3Client talks directly to a mailbox where you can retrieve, move, delete emails. There a lot of similarities between 
the ImapClient and this one, but the POP3 protocol have some restrictions or limitations since is an older protocol and it's oriented
for local working.

```java
Pop3Client client = new Pop3Client("juan", "desimoni", "juan.pop3.host", Pop3Client.DEFAULT_POP3S_PORT, new ClientConfiguration());
```

Of course we also have a retrieve operation like the ImapClient, the difference between the Imap operation and this one, is that the 
POP3 transport does not provide the capability to NOT open the fetched emails, so all the emails are READ and opened always. (emails are not
really read in POP3 since the concept does not exist.)

```java
pop3Client.retrieve(EmailConstants.INBOX_FOLDER);
```

### More complex configurations

The ClientConfiguration enables the construction of more complex configurations to enable encrypted conections using TLS/SSL
and injecting custom properties to the underlying protocol if requeried.

