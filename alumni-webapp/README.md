# Alumni WebApp

## Signicat Test Users

| National ID  | Provider  | Last name  | First name  | One-time password  | Password  |
|---|---|---|---|---|---|
| 11113306361	| Signicat	| Johnson	| John	| otp	| qwer1234 |
| 29090816894	| Signicat	| Williams	| Ellie	| otp	| qwer1234 |
| 10103933108	| Signicat	| Nordmann	| Ola	| otp	| qwer1234 |


## Setup development

The simplest way to deliver all required configuration is to execute the following instructions:

1. Create the .env.public file.

``
cp env.public.sample .env.public
``

And provide required values which will be transformed and served from the /public site.  

2. Create .env.local file for process environment properties:

``
touch .env.local
``

The file may contain the following props:

```
PORT=5000
REACT_APP_API_TARGET=http://localhost:21001
```

### OpenAPI schema generator

1. Fetch the newest version of openapi and overwrite ``api-docs/openapi.json``
2. ``npm run generate-api``

## Cypress E2E testing

Before E2E tests can be performed, configure the required environment properties.
You can find a set of these properties in ``cypress.env.json.sample``. Make a copy and edit it accordingly.

```
cp cypress.env.json.sample cypress.env.json
```

An example may look similar to:

```
{
  "authority": "https://alumnidev.b2clogin.com/alumnidev.onmicrosoft.com/b2c_1_ropc/oauth2/v2.0/token",
  "clientId": "677d5d45-3372-4bf9-8d14-3eff33e462ca",
  "clientSecret": "someSecretCode",
  "scopes": ["677d5d45-3372-4bf9-8d14-3eff33e462ca"],

  "environment": "alumnidev.b2clogin.com",
  "tenantId": "77d07e45-f17a-481f-be82-e33754083f12",

  "users": {
    "pristine": {
      "username": "pristine_alumnidev@dev.null",
      "password": "secretPassword"
    },
    "nooptin": {
      "username": "nooptin_alumnidev@dev.null",
      "password": "secretPassword"
    },    
    "main": {
      "username": "main_alumnidev@dev.null",
      "password": "secretPassword"
    }
  }
}
```

The following users must be configured for the tests to work:
* _pristine_ - a pure AD account, no signicat verification passed
* _nooptin_ - AD account after signicat verification but without data in the alumni DB.
* _main_ - AD account after signicat verification with data in the alumni DB.  

```
curl --location --request POST 'http://localhost:21000/sync' \
--header 'Content-Type: application/json' \
--data-raw '{
    "zalarisId": "xyz",
    "ssid": "RokqHz7OjZFv1RE2vY5hrY7ZhBXhwyAA"
}'
```


# Getting Started with Create React App

This project was bootstrapped with [Create React App](https://github.com/facebook/create-react-app).

## Available Scripts

In the project directory, you can run:

### `npm start`

Runs the app in the development mode.\
Open [http://localhost:3000](http://localhost:3000) to view it in the browser.

The page will reload if you make edits.\
You will also see any lint errors in the console.

### `npm test`

Launches the test runner in the interactive watch mode.\
See the section about [running tests](https://facebook.github.io/create-react-app/docs/running-tests) for more information.

### `npm run build`

Builds the app for production to the `build` folder.\
It correctly bundles React in production mode and optimizes the build for the best performance.

The build is minified and the filenames include the hashes.\
Your app is ready to be deployed!

See the section about [deployment](https://facebook.github.io/create-react-app/docs/deployment) for more information.

### `npm run eject`

**Note: this is a one-way operation. Once you `eject`, you can’t go back!**

If you aren’t satisfied with the build tool and configuration choices, you can `eject` at any time. This command will remove the single build dependency from your project.

Instead, it will copy all the configuration files and the transitive dependencies (webpack, Babel, ESLint, etc) right into your project so you have full control over them. All of the commands except `eject` will still work, but they will point to the copied scripts so you can tweak them. At this point you’re on your own.

You don’t have to ever use `eject`. The curated feature set is suitable for small and middle deployments, and you shouldn’t feel obligated to use this feature. However we understand that this tool wouldn’t be useful if you couldn’t customize it when you are ready for it.

## Learn More

You can learn more in the [Create React App documentation](https://facebook.github.io/create-react-app/docs/getting-started).

To learn React, check out the [React documentation](https://reactjs.org/).
