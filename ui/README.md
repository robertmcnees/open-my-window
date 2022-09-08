#Overview

### Building for Production

When preparing to deploy the app to production, use the `production` profile.  This is becuase Vaadin requires configuration whendeploying to a production environment.

```
mvn clean package -Pproduction
```