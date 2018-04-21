# Trebek

Trebek is a web application to help you study and hone your Jeopardy knowledge. A powerful search engine lets you test yourself against categories and relevant questions. Alternatively, if you leave the search field blank then Trebek will provide you with a random stream of questions.

Initially developed for my personal amusement, data was gathered from J-Archive, a fantastic resource. Currently, you will need to provide your own data, however. Do note that as long as your data is properly formated, Trebek can be used as a quiz-like application for pretty much anything.

There are a number of features that can be added to Trebek. Potential paths for additional development include:
 - User accounts
 - Score keeping
 - Analysis to help identify weak areas
 - Links to corresponding Wikipedia pages for answers
 - Shareable user-defined lists of questions
 - Keybindings to improve navigation
 - Social media links
 - Graph based exploration of data
 - Reporting buttons (e.g., wrong answer, bug report)
 - Redesigned UI
 - Tagging of questions and answers by users
 - Dataset Store
 - Allow content to be submitted by users

## Prerequisites

You'll need the following installed in order to run Trebek:

- Clojure and Leinigen
- Elasticsearch 5.2.x
- elasticsearch-head
- npm
- grunt

## Development

Configuration is stored in `env/dev/resources/config.edn` and `env/dev/prod/config.edn` for the respective profiles. These config files are tracked in source control... all sensitive config should be untracked and located elsewhere. See `yogthos/config` and `environ` for more information. With an uberjar, an additional config file can be passed in via something like the following snippet for the Java command: `-Dconfig="config.edn"`. This will merge and overwrite the default config values.

First make sure elasticsearch (ES) is running, where ever and how ever they are located is up to you. If you were running one node on your local machine and not as a daemon, then something like `./elasticsearch-5.2.1/bin/elasticsearch` in the appropriate ES directory might suffice.

It would be prudent to feed ES your data at this point. See the section on Data Ingestion.

If you installed elasticsearch-head then the following commands are of use:

```
cd ../elasticsearch-head
npm install
grunt server
open http://localhost:9100/
```

Note: You will need grunt-cli: `npm install -g grunt-cli` and you may have to disable CORS. See their github repo for additional information.

Finally, start the web application by running `lein figwheel` and `lein run` in two separate consoles. A certain profile can be selected like so for the dev profile `lein with-profile dev run`.

Standard Addresses:

Website - localhost:3000
Elasticsearch - localhost:9200
Elasticsearch-head - localhost:9100

## Production

To run in production, you'll want to set up the ES nodes as necessary. At least two nodes in a cluster is advised... but I am not your mother so do what you want.

Provide a `config.edn` file in the Trebek project directory containing at least the following parameters for the Trebek server instance:

- ES database url and port (e.g., http://localhost:9200)
- Server port for Trebek

Run the following commands to build and launch Trebek.

```
lein uberjar
java -Dconfig="config.edn" -jar ./target/trebek.jar
```

A certain profile can be selected like so for the dev profile `lein with-profile dev uberjar`.

To deploy to Heroku, via the Heroku CLI: `git push heroku master`

The ES url can be accessed via System variables with `env`. Don't commit this url... Bonsai ES HTTPS port is 443

## Data Ingestion



## Misc links

- Super useful regarding flexbox `https://css-tricks.com/snippets/css/a-guide-to-flexbox/`
- Reagent tutorial `http://lotabout.me/orgwiki/reagent.html`
- js interop which i constantly forget `http://www.spacjer.com/blog/2014/09/12/clojurescript-javascript-interop/`
