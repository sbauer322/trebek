## Things To Do

Immediate:

- [x] Add data store
- [x] Populate data store... maybe see `http://stackoverflow.com/questions/15936616/import-index-a-json-file-into-elasticsearch` and `http://stackoverflow.com/questions/25887850/random-document-in-elasticsearch`
- [ ] Build initial API (just get at the moment (?))
  - [ ] Look into Swagger
- [x] Retrieve question and answer content from server to display on page
- [ ] Keep score (start with running score per instance... no sign in yet)
  - [ ] Will need running list of already visited question ids
  - [ ] Look into sessions for keeping track of who is who
- [ ] Main page design / UI / UX
  - [x] CSS all the things... remember progressive enhancement
  - [x] Find graphics/icons/fonts
  - [x] Wire up navigation buttons
  - [ ] Add keybindings for prevous, next, and reveal (i.e., `<-, ->, and enter`)
- [ ] Build out static content
  - [x] Add About page content
  - [x] Add Donate page content
  - [ ] Add social media links instead of Discuss page/forum

After That:

- Add log-in/credentials via OAuth (?)
  - Switch random-question generation to user or session id?
- Wire reporting buttons (e.g., wrong answer, report a bug)
- Add linking to wikipedia articles based on answer contents (?)
- Figure out how to make collapsible Learn section (UI)
- Allow exploration of data (Elasticsearch / graphs?)
- Build custom question/answer stacks? Make shareable via link?

## Development

Start web application:

```
lein figwheel
lein run
```

Start elasticsearch:

```
./elasticsearch-5.2.1/bin/elasticsearch
```

Web front end for elasticsearch - elasticsearch-head:

```
;; from trebek dir
cd ../elasticsearch-head
npm install
grunt server
open http://localhost:9100/
```

Will need grunt-cli: `npm install -g grunt-cli`
Have to disable CORS. See github


Addresses:

Website - localhost:3000
Elasticsearch - localhost:9200
Elasticsearch-head - localhost:9100

# Misc links

- Super userful regarding flexbox `https://css-tricks.com/snippets/css/a-guide-to-flexbox/`
- Reagent tutorial `http://lotabout.me/orgwiki/reagent.html`
- js interop which i constantly forget `http://www.spacjer.com/blog/2014/09/12/clojurescript-javascript-interop/`
