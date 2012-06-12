# My Github Notifier

A simple Play Framework application which uses the Github PubSubHubbub API to subscribe to some events:

- Fork events: When a repository is forked
- Watch events: When someone watches a repository

Note: The Web application uses this PubSubHubbub protocol to subscribe to events since these events are not supported by the standard github-services.

The events will be received by the current Web application and forwarded as email to a predefined user.

## Configure

Check the application.conf file and update the following properties.

### Mail

Update the SMTP properties to use your SMTP server (gmail works well since you will not be forked thousand times a day)

- mail.smtp.host=smtp.gmail.com
- mail.smtp.user=YOURMAIL@gmail.com
- mail.smtp.pass=YOURPASSWORD
- mail.smtp.channel=ssl

### Github

The Web application uses Basic Auth to authenticate to Github:

- github.user=YOURGITHUBLOGIN
- github.password=YOURGITHUBPASSWORD

### Notification properties

Events are translated as mail and sent to the notification.to defined email address:

- notification.from=SENDER_EMAIL
- notification.to=RECIPIENT_EMAIL

## Deploy
### Manual

1. The Web application has been developped using Play 1.2.4. You can get it from http://playframework.org.
2. Clone this repository
3. Launch 'play dependencies' then 'play run'
4. Open 'http://HOST:9000'

Please note that the Github events will be sent to your Web application only if you run it on a public Internet address: An application running on your laptop will never receive any notification...

### Heroku

Since Heroku.com supports Play applications, let's use it to push your notifier online:

1. Download and install the heroku binaries, create account, etc... Check the heroku.com documentation!
2. Follow the heroku.com deployment documentation for Play applications...

## Use

There is a single page to manage subscriptions where you can subscribe to events (forks, watchs, ...). That's all!


--
@chamerling  

